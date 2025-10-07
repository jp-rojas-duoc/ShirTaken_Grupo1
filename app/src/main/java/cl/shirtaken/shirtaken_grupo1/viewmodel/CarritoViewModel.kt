package cl.shirtaken.shirtaken_grupo1.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import cl.shirtaken.shirtaken_grupo1.model.ItemCarrito
import cl.shirtaken.shirtaken_grupo1.model.Polera

class CarritoViewModel : ViewModel() {

    private val _items: SnapshotStateList<ItemCarrito> = mutableStateListOf()
    val items: List<ItemCarrito> get() = _items

    val total: Int get() = _items.sumOf { it.precio * it.cantidad }

    fun agregar(p: Polera) {
        val existente = _items.firstOrNull { it.id == p.id }
        if (existente == null) {
            _items.add(ItemCarrito(p.id, p.nombre, p.precio, 1, p.urlImagen))
        } else {
            existente.cantidad += 1
            // fuerza recomposición
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
}
