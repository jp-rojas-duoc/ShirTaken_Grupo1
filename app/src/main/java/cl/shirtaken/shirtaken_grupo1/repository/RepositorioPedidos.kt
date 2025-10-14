package cl.shirtaken.shirtaken_grupo1.repository

import android.content.Context
import cl.shirtaken.shirtaken_grupo1.data.local.AppDb
import cl.shirtaken.shirtaken_grupo1.data.local.PedidoEntity
import cl.shirtaken.shirtaken_grupo1.data.local.PedidoItemEntity
import cl.shirtaken.shirtaken_grupo1.model.ItemCarrito

class RepositorioPedidos(context: Context) {
    private val db = AppDb.get(context)
    private val pedidoDao = db.pedidoDao()

    fun observarHistorial() = pedidoDao.observarHistorial()

    suspend fun registrarPedido(items: List<ItemCarrito>, total: Int): Long {
        val idPedido = pedidoDao.insertarPedido(
            PedidoEntity(fechaMs = System.currentTimeMillis(), total = total)
        ).toInt()
        val itemsEntity = items.map {
            PedidoItemEntity(
                pedidoId = idPedido,
                poleraId = it.id,
                nombre = it.nombre,
                precio = it.precio,
                cantidad = it.cantidad
            )
        }
        pedidoDao.insertarItems(itemsEntity)
        return idPedido.toLong()
    }
}
