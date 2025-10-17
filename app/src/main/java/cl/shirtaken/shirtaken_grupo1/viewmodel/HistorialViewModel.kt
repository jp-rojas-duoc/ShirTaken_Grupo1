package cl.shirtaken.shirtaken_grupo1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.shirtaken.shirtaken_grupo1.repository.RepositorioPedidos
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HistorialViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = RepositorioPedidos(app)
    val historial: StateFlow<List<cl.shirtaken.shirtaken_grupo1.data.local.PedidoConItems>> =
        repo.observarHistorial().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
