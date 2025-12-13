package cl.shirtaken.shirtaken_grupo1.data.remote

data class PedidoRemoto(
    val id: Int,
    val fecha: String,
    val total: Int
    // Agrega otros campos si tu backend los retorna
)
