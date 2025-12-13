package cl.shirtaken.shirtaken_grupo1.repository

import cl.shirtaken.shirtaken_grupo1.data.remote.PedidosApi
import cl.shirtaken.shirtaken_grupo1.ui.pantallas.PedidoUi

class RepositorioPedidos(private val pedidosApi: PedidosApi) {
    suspend fun obtenerPedidosBackend(): List<PedidoUi> {
        return pedidosApi.getPedidos().map {
            PedidoUi(
                id = it.id,
                fecha = it.fecha,
                total = it.total,
                estado = "Completado" // Cambia si tu backend devuelve un campo diferente para el estado
            )
        }
    }
}
