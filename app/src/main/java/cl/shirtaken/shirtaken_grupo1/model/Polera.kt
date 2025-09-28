package cl.shirtaken.shirtaken_grupo1.model

data class Polera (
    val id: Int,
    val nombre : String,
    val marca: String,
    val precio: Int ,
    val talla: String,
    val color : String ,
    val urlImagen: String,
    val conStock: Boolean = true ,
    val esFavorita : Boolean = true
)