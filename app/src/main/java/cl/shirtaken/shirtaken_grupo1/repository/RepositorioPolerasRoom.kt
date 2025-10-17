package cl.shirtaken.shirtaken_grupo1.repository

import kotlinx.coroutines.flow.firstOrNull
import android.content.Context
import cl.shirtaken.shirtaken_grupo1.data.local.AppDb
import cl.shirtaken.shirtaken_grupo1.model.Polera
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepositorioPolerasRoom(context: Context) {
    private val dao = AppDb.get(context).poleraDao()

    fun observarCatalogo(): Flow<List<Polera>> =
        dao.observarCatalogo().map { it.map { e -> e.aModelo() } }

    suspend fun obtenerPorId(id: Int): Polera? = dao.obtenerPorId(id)?.aModelo()

    suspend fun crear(p: Polera, stock: Int = if (p.conStock) 1 else 0): Int =
        dao.insertar(p.aEntity(stock)).toInt()

    suspend fun actualizar(p: Polera, stock: Int) = dao.actualizar(p.aEntity(stock))

    suspend fun eliminar(p: Polera) = dao.eliminar(p.aEntity())

    // Descuenta 'cantidad' del stock de la polera; retorna true si aplicó
    suspend fun descontarStock(poleraId: Int, cantidad: Int): Boolean =
        dao.descontarStock(poleraId, cantidad) > 0

    // NUEVO: consulta stock actual para validaciones
    suspend fun consultarStock(poleraId: Int): Int =
        dao.obtenerStock(poleraId) ?: 0

    suspend fun estaVacio(): Boolean =
        (dao.observarCatalogo().firstOrNull()?.isEmpty() ?: true)

    suspend fun poblarSiVacio() {
        if (estaVacio()) {
            val base = listOf(
                Polera(0, "Polera básica",  "ShirTaken",  9990, "M", "Negro",  "https://picsum.photos/300?1", conStock = true,  esFavorita = false),
                Polera(0, "Polera logo",    "ShirTaken", 12990, "L", "Blanco", "https://picsum.photos/300?2", conStock = true,  esFavorita = false),
                Polera(0, "Polera premium", "ShirTaken", 15990, "S", "Azul",   "https://picsum.photos/300?3", conStock = true,  esFavorita = true)
            )
            base.forEach { crear(it, stock = 10) }
        }
    }
}
