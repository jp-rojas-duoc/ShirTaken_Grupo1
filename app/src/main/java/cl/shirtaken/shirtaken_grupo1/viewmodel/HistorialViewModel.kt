package cl.shirtaken.shirtaken_grupo1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.shirtaken.shirtaken_grupo1.repository.RepositorioPedidos
import cl.shirtaken.shirtaken_grupo1.ui.pantallas.PedidoUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistorialViewModel(
    private val repo: RepositorioPedidos
) : ViewModel() {
    private val _historial = MutableStateFlow<List<PedidoUi>>(emptyList())
    val historial: StateFlow<List<PedidoUi>> = _historial

    fun cargarHistorial() {
        viewModelScope.launch {
            _historial.value = repo.obtenerPedidosBackend()
        }
    }
}
