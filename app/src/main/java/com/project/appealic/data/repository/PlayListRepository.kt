package com.project.appealic.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.project.appealic.data.dao.PlayListDao
import com.project.appealic.data.dao.UserDao
import com.project.appealic.data.database.AppDatabase
import com.project.appealic.data.model.PlayListEntity
import com.project.appealic.data.model.UserWithPlayLists

class PlayListRepository(application: Application) {
    private val db: AppDatabase = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java,"appealic"
    )
        .fallbackToDestructiveMigration()
        .build()
    private val playListDao : PlayListDao = db.platListDao()

    fun getAllUserPlayList(userId : String) : LiveData<List<UserWithPlayLists>>{
        return playListDao.getUserPlayLists(userId)
    }

    fun createNewPlayList(playList : PlayListEntity){
        return playListDao.insert(playList)
    }
}