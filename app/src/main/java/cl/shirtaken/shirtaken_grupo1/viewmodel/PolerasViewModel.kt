package cl.shirtaken.shirtaken_grupo1.viewmodel

import android.app.Application
import android.util.Log
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

    suspend fun obtenerPorId(id: Int): Polera? {
        var polera = _catalogoRemoto.value.firstOrNull { it.id == id }
        if (polera == null) {
            polera = try { repoRemoto.obtenerPolera(id.toLong()) } catch (_: Exception) { null }
        }
        return polera
    }

    fun cargarCatalogoRemoto() = viewModelScope.launch {
        try {
            val lista = repoRemoto.obtenerCatalogo()
            Log.d("PolerasVM", "Catalogo remoto: ${lista.size}")
            // Fallback demo si llega vacío (solo para depurar UI)
            _catalogoRemoto.value = if (lista.isNotEmpty()) lista else listOf(
                Polera(1,"Demo 1","Test",12990,"M","Negro","",true,false),
                Polera(2,"Demo 2","Test",13990,"L","Blanco","",true,false)
            )
        } catch (e: Throwable) {
            Log.e("PolerasVM", "Error cargarCatalogoRemoto", e)
            _catalogoRemoto.value = emptyList()
        }
    }

    suspend fun consultarStockRemoto(poleraId: Int): Int = try {
        repoRemoto.consultarStock(poleraId)
    } catch (_: Throwable) {
        0
    }

    fun sincronizarLocalDesdeRemoto(reemplazar: Boolean = true) = viewModelScope.launch {
        try {
            val remoto = repoRemoto.obtenerCatalogo()
            if (reemplazar) {
                val actual = repoLocal.observarCatalogo().firstOrNull().orEmpty()
                actual.forEach { repoLocal.eliminar(it) }
            }
            remoto.forEach { p -> repoLocal.crear(p, stock = 100) }
        } catch (_: Throwable) { }
    }
}
