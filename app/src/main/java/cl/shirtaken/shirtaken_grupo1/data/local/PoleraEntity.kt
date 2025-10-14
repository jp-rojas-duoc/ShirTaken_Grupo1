package cl.shirtaken.shirtaken_grupo1.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poleras")
data class PoleraEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val marca: String,
    val precio: Int,
    val talla: String,
    val color: String,
    val urlImagen: String,
    val stock: Int,
    val esFavorita: Boolean = false
)
