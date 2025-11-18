package cl.shirtaken.shirtaken_grupo1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.shirtaken.shirtaken_grupo1.model.Polera
import cl.shirtaken.shirtaken_grupo1.repository.RepositorioPolerasRemoto
import cl.shirtaken.shirtaken_grupo1.repository.RepositorioPolerasRoom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class PolerasViewModel(app: Application) : AndroidViewModel(app) {
    private val repoLocal = RepositorioPolerasRoom(app)
    private val repoRemoto = RepositorioPolerasRemoto()

    private val _catalogoRemoto = MutableStateFlow<List<Polera>>(emptyList())
    val catalogoRemoto: StateFlow<List<Polera>> get() = _catalogoRemoto

    init {
        cargarCatalogoRemoto()
    }

    fun crear(p: Polera, stock: Int) = viewModelScope.launch { repoLocal.crear(p, stock) }
    fun actualizar(p: Polera, stock: Int) = viewModelScope.launch { repoLocal.actualizar(p, stock) }
    fun eliminar(p: Polera) = viewModelScope.launch { repoLocal.eliminar(p) }

    // ✅ FIX: Buscar directamente en el backend si no está en catálogo local
    suspend fun obtenerPorId(id: Int): Polera? {
        // Primero intenta en el catálogo remoto en memoria
        var polera = _catalogoRemoto.value.firstOrNull { it.id == id }

        // Si no está, consulta directamente al backend
        if (polera == null) {
            polera = try {
                repoRemoto.obtenerPolera(id.toLong())
            } catch (e: Exception) {
                null
            }
        }

        return polera
    }

    fun cargarCatalogoRemoto() = viewModelScope.launch {
        try {
            val lista = repoRemoto.obtenerCatalogo()
            _catalogoRemoto.value = lista
        } catch (e: Throwable) {
            _catalogoRemoto.value = emptyList()
        }
    }

    suspend fun consultarStockRemoto(poleraId: Int): Int = try {
        repoRemoto.consultarStock(poleraId)
    } catch (e: Throwable) {
        0
    }

    fun sincronizarLocalDesdeRemoto(reemplazar: Boolean = true) = viewModelScope.launch {
        try {
            val remoto = repoRemoto.obtenerCatalogo()
            if (reemplazar) {
                val actual = repoLocal.observarCatalogo().firstOrNull().orEmpty()
                actual.forEach { repoLocal.eliminar(it) }
            }
            remoto.forEach { p ->
                repoLocal.crear(p, stock = 100)
            }
        } catch (_: Throwable) {
            // Ignora errores
        }
    }
}
