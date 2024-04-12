package com.project.appealic.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.project.appealic.data.dao.SongDao
import com.project.appealic.data.dao.UserDao
import com.project.appealic.data.database.AppDatabase
import com.project.appealic.data.model.SongEntity
import com.spotify.protocol.types.Track
import kotlinx.coroutines.tasks.await
import okhttp3.Callback

class SongRepository(application: Application) {

    val firebaseDB = Firebase.firestore

    private val db: AppDatabase = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java,"appealic"
    )
        .fallbackToDestructiveMigration()
        .build()
    private val songDao : SongDao = db.songDao()


    fun getAllTrack(): Task<QuerySnapshot> {
        return firebaseDB.collection("tracks").get()
    }

    fun getAllArtist(): Task<QuerySnapshot>{
        return firebaseDB.collection("artists").get()
    }

    fun getRecentSongs(userId: String): LiveData<List<SongEntity>> {
        return songDao.getRecentSongs(userId)
    }
    fun getLikedSongs(userId: String): Task<QuerySnapshot> {
        return firebaseDB.collection("songs")
            .whereEqualTo("userId", userId)
            .whereEqualTo("liked", true)
            .get()
    }


    suspend fun insertSong(song: SongEntity) {
        songDao.insertSong(song)
    }
}