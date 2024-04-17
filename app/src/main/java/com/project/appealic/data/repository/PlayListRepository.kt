package com.project.appealic.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.project.appealic.data.dao.PlayListDao
import com.project.appealic.data.dao.UserDao
import com.project.appealic.data.database.AppDatabase
import com.project.appealic.data.model.PlayListEntity
import com.project.appealic.data.model.Track
import com.project.appealic.data.model.UserWithPlayLists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlayListRepository(application: Application) {
    val firebaseDB = Firebase.firestore
    private val db: AppDatabase = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java,"appealic"
    )
        .fallbackToDestructiveMigration()
        .build()
    private val playListDao : PlayListDao = db.platListDao()

    fun getAllUserPlayList(userId : String) : LiveData<List<PlayListEntity>> {
        return playListDao.getUserPlayLists(userId)
    }


    fun createNewPlayList(playList : PlayListEntity){
        return playListDao.insert(playList)
    }

    fun getAllPlaylists(): Task<QuerySnapshot> {
        return firebaseDB.collection("playlists").get()
    }
    suspend fun addTrackToPlaylist(track: Track, playList: PlayListEntity) {
        withContext(Dispatchers.IO) {
            if (!playList.tracks.contains(track)) {
                playList.tracks.add(track)
                playListDao.update(playList)
            }
        }
    }
}