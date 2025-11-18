package cl.shirtaken.shirtaken_grupo1.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import com.google.gson.annotations.SerializedName
import android.util.Log

// DTO para respuesta del clima
data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val current: CurrentWeather?
)

data class CurrentWeather(
    val temperature: Double,
    @SerializedName("weather_code")
    val weatherCode: Int,
    @SerializedName("wind_speed")
    val windSpeed: Double? = null
)

// Interfaz Retrofit
interface WeatherApi {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature,weather_code",
        @Query("temperature_unit") unit: String = "celsius"
    ): WeatherResponse
}

// Crear instancia
private const val TAG = "WeatherApi"

private val weatherLoggingInterceptor = HttpLoggingInterceptor { message ->
    Log.d(TAG, message)
}.apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val weatherHttpClient = OkHttpClient.Builder()
    .addInterceptor(weatherLoggingInterceptor)
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

private val retrofitWeather = Retrofit.Builder()
    .baseUrl("https://api.open-meteo.com/v1/")
    .client(weatherHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

fun provideWeatherApi(): WeatherApi {
    Log.d(TAG, "Creando instancia de WeatherApi")
    return retrofitWeather.create(WeatherApi::class.java)
}
