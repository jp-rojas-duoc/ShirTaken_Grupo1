package cl.shirtaken.shirtaken_grupo1.viewmodel

import android.view.View
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import cl.shirtaken.shirtaken_grupo1.model.Polera
import cl.shirtaken.shirtaken_grupo1.repository.RepositorioPoleras

class PolerasViewModel (
    private val repo: RepositorioPoleras = RepositorioPoleras()
): ViewModel() {
    private val _catalogo = mutableStateOf<List<Polera>>(emptyList())
    val catalogo: State<List<Polera>> = _catalogo

    fun cargar() {_catalogo.value = repo.obtenerTodas()}
    fun obtenerPorId(id: Int): Polera? = repo.obtenerPorId(id)

    fun alternarFavorito(id: Int) {
        _catalogo.value = _catalogo.value.map {
            if (it.id == id) it.copy(esFavorita = !it.esFavorita) else  it
        }
    }
}
