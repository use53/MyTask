package uz.myapigrid.taskmy.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [IconModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun iconDao(): IconDao
}