package cl.shirtaken.shirtaken_grupo1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cl.shirtaken.shirtaken_grupo1.repository.RepositorioPedidos

class HistorialViewModelFactory(
    private val repo: RepositorioPedidos
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistorialViewModel::class.java)) {
            return HistorialViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
