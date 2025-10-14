package cl.shirtaken.shirtaken_grupo1.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedido_items")
data class PedidoItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pedidoId: Int,
    val poleraId: Int,
    val nombre: String,
    val precio: Int,
    val cantidad: Int
)
