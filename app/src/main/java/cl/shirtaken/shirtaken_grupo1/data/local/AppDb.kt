package cl.shirtaken.shirtaken_grupo1.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PoleraEntity::class, PedidoEntity::class, PedidoItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun poleraDao(): PoleraDao
    abstract fun pedidoDao(): PedidoDao

    companion object {
        @Volatile private var INSTANCE: AppDb? = null
        fun get(context: Context): AppDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    "shirtaken.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
    }
}
