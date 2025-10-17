package cl.shirtaken.shirtaken_grupo1.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.shirtaken.shirtaken_grupo1.model.ItemCarrito
import cl.shirtaken.shirtaken_grupo1.model.Polera
import cl.shirtaken.shirtaken_grupo1.repository.RepositorioPedidos
import cl.shirtaken.shirtaken_grupo1.repository.RepositorioPolerasRoom
import kotlinx.coroutines.launch

class CarritoViewModel(app: Application) : AndroidViewModel(app) {

    private val repoPoleras = RepositorioPolerasRoom(app)
    private val repoPedidos = RepositorioPedidos(app)

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

    // Suma validando stock; notifica por callback si no alcanza
    fun sumar(id: Int, onSinStock: (() -> Unit)? = null) {
        val item = _items.find { it.id == id } ?: return
        viewModelScope.launch {
            // Consulta stock actual en BD y compara con lo que quieres dejar
            val stockDisponible = try { repoPoleras.consultarStock(item.id) } catch (_: Throwable) { 0 }
            if (stockDisponible >= item.cantidad + 1) {
                item.cantidad += 1
                _items[_items.indexOf(item)] = item.copy()
            } else {
                onSinStock?.invoke()
            }
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

    // Guarda pedido + descuenta stock; notifica resultado
    fun confirmarCompra(
        onOk: (Long) -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch {
        try {
            if (_items.isEmpty()) throw IllegalStateException("Carrito vacío")
            // Descontar stock por cada ítem; si alguno falla, aborta
            for (it in _items) {
                val ok = repoPoleras.descontarStock(it.id, it.cantidad)
                if (!ok) throw IllegalStateException("Sin stock para ${it.nombre}")
            }
            // Registrar pedido e items
            val idPedido = repoPedidos.registrarPedido(_items.toList(), total)
            // Limpiar carrito
            limpiar()
            onOk(idPedido)
        } catch (t: Throwable) {
            onError(t.message ?: "Error al confirmar compra")
        }
    }
}
