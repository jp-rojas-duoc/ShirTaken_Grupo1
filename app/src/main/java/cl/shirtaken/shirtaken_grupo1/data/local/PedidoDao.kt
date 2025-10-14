package cl.shirtaken.shirtaken_grupo1.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

data class PedidoConItems(
    @Embedded val pedido: PedidoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "pedidoId",
        entity = PedidoItemEntity::class
    )
    val items: List<PedidoItemEntity>
)

@Dao
interface PedidoDao {
    @Insert
    suspend fun insertarPedido(p: PedidoEntity): Long

    @Insert
    suspend fun insertarItems(items: List<PedidoItemEntity>)

    @Transaction
    @Query("SELECT * FROM pedidos ORDER BY id DESC")
    fun observarHistorial(): Flow<List<PedidoConItems>>
}
