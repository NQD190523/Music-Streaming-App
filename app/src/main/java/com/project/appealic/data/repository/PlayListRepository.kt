package com.project.appealic.data.repository

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.project.appealic.data.dao.PlayListDao
import com.project.appealic.data.dao.SongDao
import com.project.appealic.data.database.AppDatabase
import com.project.appealic.data.model.PlayListEntity
import com.project.appealic.data.model.Playlist
import com.project.appealic.data.model.Track

class PlayListRepository(application: Application) {
    private val firebaseDB = Firebase.firestore
    private val db: AppDatabase = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java, "appealic"
    )
        .fallbackToDestructiveMigration()
        .build()
    private val playlistDao: PlayListDao = db.playListDao()

    fun createNewPlaylist(playlist: PlayListEntity) {
        playlistDao.insert(playlist)
    }

    fun getAllUserPlayList(userId: String): List<PlayListEntity> {
        return  playlistDao.getUserPlayLists(userId)
    }


    fun getAllPlaylists(): Task<QuerySnapshot> {
        return firebaseDB.collection("playlists").get()
    }

    fun getTracksFromPlaylist(playlistId: String): Task<DocumentSnapshot> {
        return firebaseDB.collection("playlists").document(playlistId).get()
    }

    fun loadPlaylistSearchResults(searchQuery: String?): LiveData<List<Playlist>> {
        val searchResultsLiveData = MutableLiveData<List<Playlist>>()
        if (searchQuery.isNullOrEmpty()) {
            Log.d(TAG, "loadPlaylistSearchResults: No search query provided")
            return searchResultsLiveData
        }

        val playlistsTask = firebaseDB.collection("playlists").get()
        playlistsTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val playlistsSnapshot = task.result
                val playlists = playlistsSnapshot?.documents?.mapNotNull { it.toObject(Playlist::class.java) } ?: emptyList()
                val filteredPlaylists = playlists.filter { it.playlistName?.contains(searchQuery, ignoreCase = true) == true }
                searchResultsLiveData.postValue(filteredPlaylists)
            } else {
                Log.e(TAG, "loadPlaylistSearchResults: Error fetching data", task.exception)
            }
        }

        return searchResultsLiveData
    }

    fun addTrackToPlaylist( playlist: PlayListEntity) {
        playlistDao.update(playlist)
    }
}