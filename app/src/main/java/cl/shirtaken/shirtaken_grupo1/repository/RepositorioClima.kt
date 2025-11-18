package cl.shirtaken.shirtaken_grupo1.repository

import android.util.Log
import cl.shirtaken.shirtaken_grupo1.data.remote.WeatherApi
import cl.shirtaken.shirtaken_grupo1.data.remote.provideWeatherApi

class RepositorioClima(
    private val api: WeatherApi = provideWeatherApi()
) {
    companion object {
        private const val TAG = "RepositorioClima"
    }

    suspend fun obtenerClimaActual(
        latitude: Double = -33.8688,
        longitude: Double = -51.2093
    ): String = try {
        Log.d(TAG, "Solicitando clima para lat=$latitude, lon=$longitude")

        val response = api.getWeather(latitude, longitude)

        Log.d(TAG, "Respuesta recibida: $response")

        val temp = response.current?.temperature?.toInt() ?: 0
        val code = response.current?.weatherCode ?: 0  // ✅ Usar weatherCode
        val descripcion = getClimDescription(code)

        val resultado = "🌡️ $temp°C - $descripcion"
        Log.d(TAG, "Clima calculado: $resultado")
        resultado

    } catch (e: Exception) {
        Log.e(TAG, "Error obteniendo clima", e)
        e.printStackTrace()
        "🌤️ Clima no disponible"
    }

    private fun getClimDescription(code: Int): String = when (code) {
        0 -> "☀️ Soleado"
        1, 2 -> "⛅ Parcialmente nublado"
        3 -> "☁️ Nublado"
        45, 48 -> "🌫️ Niebla"
        51, 53, 55 -> "🌧️ Lluvia ligera"
        61, 63, 65 -> "🌧️ Lluvia"
        71, 73, 75 -> "❄️ Nieve"
        80, 81, 82 -> "⛈️ Lluvia fuerte"
        95, 96, 99 -> "⛈️ Tormenta"
        else -> "🌤️ Desconocido (código: $code)"
    }
}
