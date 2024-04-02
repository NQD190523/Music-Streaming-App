package com.project.appealic.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.appealic.data.dao.SongDao
import com.project.appealic.data.dao.UserDao
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.model.UserEntity

@Database(entities = [UserEntity::class, SongEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun songDao() : SongDao
}