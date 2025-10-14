package cl.shirtaken.shirtaken_grupo1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.shirtaken.shirtaken_grupo1.model.Polera
import cl.shirtaken.shirtaken_grupo1.repository.RepositorioPolerasRoom
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PolerasViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = RepositorioPolerasRoom(app)

    val catalogo: StateFlow<List<Polera>> =
        repo.observarCatalogo().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    init {
        viewModelScope.launch { repo.poblarSiVacio() }
    }

    fun crear(p: Polera, stock: Int) = viewModelScope.launch { repo.crear(p, stock) }
    fun actualizar(p: Polera, stock: Int) = viewModelScope.launch { repo.actualizar(p, stock) }
    fun eliminar(p: Polera) = viewModelScope.launch { repo.eliminar(p) }

    suspend fun obtenerPorId(id: Int): Polera? = repo.obtenerPorId(id)
}
