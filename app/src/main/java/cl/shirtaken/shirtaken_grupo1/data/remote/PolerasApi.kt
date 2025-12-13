package cl.shirtaken.shirtaken_grupo1.data.remote

import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import cl.shirtaken.shirtaken_grupo1.model.Polera

// DTO que coincide con tu backend
data class PoleraDto(
    val id: Long? = null,
    val nombre: String,
    val marca: String,
    val precio: Int,
    val talla: String,
    val color: String,
    val urlImagen: String,
    val stock: Int,
    val esFavorita: Boolean
)

interface PolerasApi {
    @GET("api/poleras")
    suspend fun obtenerPoleras(): List<PoleraDto>

    @GET("api/poleras/{id}")
    suspend fun obtenerPolera(@Path("id") id: Long): PoleraDto

    @POST("api/poleras")
    suspend fun crearPolera(@Body dto: PoleraDto): PoleraDto

    @PUT("api/poleras/{id}")
    suspend fun actualizarPolera(@Path("id") id: Long, @Body dto: PoleraDto): PoleraDto

    @DELETE("api/poleras/{id}")
    suspend fun eliminarPolera(@Path("id") id: Long)

    @GET("api/poleras/{id}/stock")
    suspend fun consultarStock(@Path("id") id: Long): Int

    @PUT("api/poleras/{id}/stock")
    suspend fun descontarStock(
        @Path("id") id: Long,
        @Query("cantidad") cantidad: Int
    ): Boolean
}

// Logging interceptor
private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

// OkHttpClient
private val httpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

// IMPORTANTE: baseUrl para dispositivo físico
// Si pruebas en emulador usa "http://10.0.2.2:8080/"
// En teléfono físico, tu IP LAN (asegúrate que el teléfono accede a ese host)
private const val BASE_URL = "http://10.220.177.54:8080/"



private val retrofitPoleras: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(httpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

fun providePolerasApi(): PolerasApi = retrofitPoleras.create(PolerasApi::class.java)

// Conversión robusta DTO -> Modelo (evita id=0 y limpia espacios)
fun PoleraDto.aModelo(index: Int = 0): Polera = Polera(
    id = (this.id ?: (index + 1).toLong()).toInt(),
    nombre = this.nombre.trim(),
    marca = this.marca.trim(),
    precio = this.precio,
    talla = this.talla.trim(),
    color = this.color.trim(),
    urlImagen = this.urlImagen.trim(),
    conStock = this.stock > 0,
    esFavorita = this.esFavorita
)
