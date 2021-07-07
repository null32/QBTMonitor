package space.skycolor.qbtmonitor.entity

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ConnectionDao {
    @Query("SELECT * FROM connections ORDER BY id")
    fun getItems(): Flow<List<ConnectionEntity>>

    @Query("SELECT * FROM connections WHERE id = :id")
    fun getItem(id: Int): Flow<ConnectionEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ConnectionEntity)

    @Update
    suspend fun update(item: ConnectionEntity)

    @Delete
    suspend fun delete(item: ConnectionEntity)
}