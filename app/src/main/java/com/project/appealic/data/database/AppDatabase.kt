package com.project.appealic.data.database
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.project.appealic.data.dao.PlayListDao
import com.project.appealic.data.dao.SongDao
import com.project.appealic.data.dao.UserDao
import com.project.appealic.data.model.Converters
import com.project.appealic.data.model.PlayListEntity
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.model.UserEntity


@Database(entities = [UserEntity::class, SongEntity::class, PlayListEntity::class], version = 13, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun songDao(): SongDao
    abstract fun playListDao(): PlayListDao
}