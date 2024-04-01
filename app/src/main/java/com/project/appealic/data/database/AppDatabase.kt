package com.project.appealic.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.appealic.data.dao.UserDao
import com.project.appealic.data.model.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
}