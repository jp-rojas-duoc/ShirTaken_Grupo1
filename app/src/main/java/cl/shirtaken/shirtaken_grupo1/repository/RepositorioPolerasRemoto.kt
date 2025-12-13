package cl.shirtaken.shirtaken_grupo1.repository

import android.util.Log
import cl.shirtaken.shirtaken_grupo1.data.remote.PolerasApi
import cl.shirtaken.shirtaken_grupo1.data.remote.providePolerasApi
import cl.shirtaken.shirtaken_grupo1.data.remote.aModelo
import cl.shirtaken.shirtaken_grupo1.model.Polera

class RepositorioPolerasRemoto(
    private val api: PolerasApi = providePolerasApi()
) {
    suspend fun obtenerCatalogo(): List<Polera> = try {
        val dto = api.obtenerPoleras()
        Log.d("RepoPoleras", "GET /api/poleras -> ${dto.size} elementos")
        val modelos = dto.mapIndexed { i, it -> it.aModelo(i) }
        Log.d("RepoPoleras", "Mapeados -> ${modelos.size} ids=${modelos.take(3).map { it.id }}")
        modelos
    } catch (e: Exception) {
        Log.e("RepoPoleras", "Error obtenerCatalogo", e)
        emptyList()
    }

    suspend fun consultarStock(id: Int): Int = try {
        api.consultarStock(id.toLong())
    } catch (e: Exception) {
        Log.e("RepoPoleras", "Error consultarStock($id)", e)
        0
    }

    suspend fun descontarStock(id: Int, cantidad: Int): Boolean = try {
        api.descontarStock(id.toLong(), cantidad)
    } catch (e: Exception) {
        Log.e("RepoPoleras", "Error descontarStock($id,$cantidad)", e)
        false
    }

    suspend fun obtenerPolera(id: Long): Polera? = try {
        api.obtenerPolera(id).aModelo()
    } catch (e: Exception) {
        Log.e("RepoPoleras", "Error obtenerPolera($id)", e)
        null
    }
}
