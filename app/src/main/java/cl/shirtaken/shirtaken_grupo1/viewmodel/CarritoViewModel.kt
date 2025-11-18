package cl.shirtaken.shirtaken_grupo1.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.shirtaken.shirtaken_grupo1.model.ItemCarrito
import cl.shirtaken.shirtaken_grupo1.model.Polera
import cl.shirtaken.shirtaken_grupo1.data.remote.PedidoItemDto
import cl.shirtaken.shirtaken_grupo1.data.remote.PedidoRequestDto
import cl.shirtaken.shirtaken_grupo1.data.remote.providePedidosApi
import kotlinx.coroutines.launch

class CarritoViewModel(app: Application) : AndroidViewModel(app) {

    private val _items: SnapshotStateList<ItemCarrito> = mutableStateListOf()
    val items: List<ItemCarrito> get() = _items

    val total: Int get() = _items.sumOf { it.precio * it.cantidad }

    fun agregar(p: Polera) {
        val existente = _items.firstOrNull { it.id == p.id }
        if (existente == null) {
            _items.add(ItemCarrito(p.id, p.nombre, p.precio, 1, p.urlImagen))
        } else {
            existente.cantidad += 1
            _items[_items.indexOf(existente)] = existente.copy()
        }
    }

    fun sumar(id: Int) {
        _items.find { it.id == id }?.let {
            it.cantidad += 1
            _items[_items.indexOf(it)] = it.copy()
        }
    }

    fun restar(id: Int) {
        _items.find { it.id == id }?.let {
            if (it.cantidad > 1) {
                it.cantidad -= 1
                _items[_items.indexOf(it)] = it.copy()
            } else {
                eliminar(id)
            }
        }
    }

    fun eliminar(id: Int) {
        _items.removeAll { it.id == id }
    }

    fun limpiar() = _items.clear()

    // Enviar pedido al backend
    fun enviarPedidoAlBackend(
        nombreCliente: String,
        email: String,
        telefono: String,
        direccion: String?,
        onOk: (Long) -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch {
        try {
            if (_items.isEmpty()) {
                onError("Carrito vacío")
                return@launch
            }

            // Construir DTOs
            val itemsDto = _items.map { itc ->
                PedidoItemDto(
                    poleraId = itc.id.toLong(),
                    cantidad = itc.cantidad,
                    precioUnitario = itc.precio
                )
            }

            val req = PedidoRequestDto(
                nombreCliente = nombreCliente,
                email = email,
                telefono = telefono,
                direccion = direccion,
                items = itemsDto,
                total = total
            )

            // Llamar API
            val api = providePedidosApi()
            val resp = api.crearPedido(req)

            // Limpiar carrito
            limpiar()

            // Éxito
            onOk(resp.id)

        } catch (e: Exception) {
            e.printStackTrace()
            onError(e.message ?: "Error desconocido al crear pedido")
        }
    }
}
