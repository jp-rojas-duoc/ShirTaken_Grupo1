package cl.shirtaken.shirtaken_grupo1.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PoleraDao {
    @Query("SELECT * FROM poleras ORDER BY id DESC")
    fun observarCatalogo(): Flow<List<PoleraEntity>>

    @Query("SELECT * FROM poleras WHERE id = :id")
    suspend fun obtenerPorId(id: Int): PoleraEntity?

    @Insert
    suspend fun insertar(p: PoleraEntity): Long

    @Update
    suspend fun actualizar(p: PoleraEntity)

    @Delete
    suspend fun eliminar(p: PoleraEntity)

    @Query("UPDATE poleras SET stock = stock - :cantidad WHERE id = :poleraId AND stock >= :cantidad")
    suspend fun descontarStock(poleraId: Int, cantidad: Int): Int
}
