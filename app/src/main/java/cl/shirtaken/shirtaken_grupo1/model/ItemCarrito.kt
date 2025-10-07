package cl.shirtaken.shirtaken_grupo1.model

data class ItemCarrito(
    val id: Int,          // mismo id de la polera
    val nombre: String,
    val precio: Int,
    var cantidad: Int = 1,
    val urlImagen: String
)
