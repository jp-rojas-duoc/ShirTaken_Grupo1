package cl.shirtaken.shirtaken_grupo1.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

data class PedidoItemDto(
    val poleraId: Long,
    val cantidad: Int,
    val precioUnitario: Int
)

data class PedidoRequestDto(
    val nombreCliente: String,
    val email: String,
    val telefono: String,
    val direccion: String?,
    val items: List<PedidoItemDto>,
    val total: Int
)

data class PedidoResponseDto(
    val id: Long,
    val fecha: String,
    val total: Int
)

interface PedidosApi {
    @POST("api/pedidos")
    suspend fun crearPedido(@Body request: PedidoRequestDto): PedidoResponseDto
}

// Logging interceptor para debug
private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

// OkHttpClient con timeout más largo
private val httpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

// Instancia única de Retrofit
private val retrofit = Retrofit.Builder()
    .baseUrl("http://192.168.1.136:8080/")  // ✅ CAMBIO: Tu IP real
    .client(httpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

fun providePedidosApi(): PedidosApi =
    retrofit.create(PedidosApi::class.java)
