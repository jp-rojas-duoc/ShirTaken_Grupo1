package cl.shirtaken.shirtaken_grupo1.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedidos")
data class PedidoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fechaMs: Long,
    val total: Int
)
