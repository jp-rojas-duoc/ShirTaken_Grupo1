package cl.shirtaken.shirtaken_grupo1.repository

import cl.shirtaken.shirtaken_grupo1.data.remote.PolerasApi
import cl.shirtaken.shirtaken_grupo1.data.remote.providePolerasApi
import cl.shirtaken.shirtaken_grupo1.model.Polera

class RepositorioPolerasRemoto(
    private val api: PolerasApi = providePolerasApi()
) {
    suspend fun obtenerCatalogo(): List<Polera> = api.obtenerPoleras().map { it.aModelo() }
    suspend fun consultarStock(id: Int): Int = api.consultarStock(id.toLong())
    suspend fun descontarStock(id: Int, cantidad: Int): Boolean = api.descontarStock(id.toLong(), cantidad)

    // ✅ AGREGAR ESTE MÉTODO
    suspend fun obtenerPolera(id: Long): Polera? =
        try {
            api.obtenerPolera(id).aModelo()
        } catch (e: Exception) {
            null
        }

}
