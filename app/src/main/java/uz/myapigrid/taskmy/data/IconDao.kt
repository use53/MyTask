package uz.myapigrid.taskmy.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IconDao {

    @Insert
    suspend fun saveItem(icon: IconModel): Long?

    @Insert
    suspend fun saveAllItem(icons: List<IconModel>)

    @Query("select * from icon")
    suspend fun getAll(): List<IconModel>

    @Query("delete from icon where columnId=:columnId and rowId=:rowId")
    suspend fun deleteIcon(columnId: Long, rowId: Long)

    @Query("select * from icon where columnId=:columnId and rowId=:rowId")
    suspend fun getIcon(columnId: Long, rowId: Long): IconModel

    @Query("update icon set columnId=:toColumn, rowId=:toRow where columnId=:fromColumn and rowId=:fromRow")
    suspend fun updateIcon(fromColumn: Long, fromRow: Long, toColumn: Long, toRow: Long)

}