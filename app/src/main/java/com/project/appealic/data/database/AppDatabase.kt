package com.project.appealic.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.project.appealic.data.dao.PlayListDao
import com.project.appealic.data.dao.SongDao
import com.project.appealic.data.dao.UserDao
import com.project.appealic.data.model.PlayListEntity
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.model.UserEntity

@Database(entities = [UserEntity::class, SongEntity::class, PlayListEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun songDao() : SongDao
    abstract fun platListDao() : PlayListDao

//    companion object {
//        val migration1to2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                // Thực hiện các thay đổi cần thiết trong cấu trúc của bảng SongEntity
//                database.execSQL("ALTER TABLE songentity ADD COLUMN new_column_name ")
//            }
//        }
//    }
}