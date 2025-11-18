package cl.shirtaken.shirtaken_grupo1.data.remote

import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import cl.shirtaken.shirtaken_grupo1.model.Polera
import java.util.concurrent.TimeUnit

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
    suspend fun descontarStock(@Path("id") id: Long, @Query("cantidad") cantidad: Int): Boolean
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

// Retrofit instance para Póleras
private val retrofitPoleras = Retrofit.Builder()
    .baseUrl("http://192.168.1.136:8080/")  // ✅ CAMBIO: Tu IP real
    .client(httpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

fun providePolerasApi(): PolerasApi =
    retrofitPoleras.create(PolerasApi::class.java)

// ✅ AGREGAR ESTA EXTENSIÓN - Convierte DTO a Modelo
fun PoleraDto.aModelo(): Polera = Polera(
    id = this.id?.toInt() ?: 0,
    nombre = this.nombre,
    marca = this.marca,
    precio = this.precio,
    talla = this.talla,
    color = this.color,
    urlImagen = this.urlImagen,
    conStock = this.stock > 0,
    esFavorita = this.esFavorita
)
