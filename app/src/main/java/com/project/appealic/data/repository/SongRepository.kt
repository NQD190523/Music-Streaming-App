package com.project.appealic.data.repository

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
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


    fun insertSong(song: SongEntity) {
        songDao.insertSong(song)
    }

//    fun loadSearchResults(searchQuery: String?): LiveData<List<com.project.appealic.data.model.Track>> {
//        val tracksLiveData = MutableLiveData<List<com.project.appealic.data.model.Track>>()
//        val query = searchQuery?.let {
//            Log.d(TAG, "loadSearchResults: Searching for tracks with query: $it")
//            firebaseDB.collection("tracks")
//                .whereGreaterThanOrEqualTo("trackTitle", it)
//                .whereLessThanOrEqualTo("trackTitle", it + "\uf8ff")
//        }
//        query?.addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
//            if (error != null) {
//                Log.e(TAG, "loadSearchResults: Error fetching tracks", error)
//                return@addSnapshotListener
//            }
//
//            if (snapshot != null && !snapshot.isEmpty) {
//                Log.d(TAG, "loadSearchResults: Found ${snapshot.size()} tracks")
//                val tracks = snapshot.documents.mapNotNull { it.toObject(com.project.appealic.data.model.Track::class.java) }
//                tracksLiveData.postValue(tracks)
//            } else {
//                Log.d(TAG, "loadSearchResults: No tracks found")
//            }
//        }
//        return tracksLiveData
fun loadSearchResults(searchQuery: String?): LiveData<List<com.project.appealic.data.model.Track>> {
    val tracksLiveData = MutableLiveData<List<com.project.appealic.data.model.Track>>()
    if (searchQuery.isNullOrEmpty()) {
        Log.d(TAG, "loadSearchResults: No search query provided")
        return tracksLiveData
    }

    firebaseDB.collection("tracks").get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val snapshot = task.result
            if (snapshot != null && !snapshot.isEmpty) {
                Log.d(TAG, "loadSearchResults: Found ${snapshot.size()} tracks")
                val tracks = snapshot.documents.mapNotNull { it.toObject(com.project.appealic.data.model.Track::class.java) }
                val filteredTracks = tracks.filter { it.trackTitle?.contains(searchQuery, ignoreCase = true) == true }
                tracksLiveData.postValue(filteredTracks)
            } else {
                Log.d(TAG, "loadSearchResults: No tracks found")
            }
        } else {
            Log.e(TAG, "loadSearchResults: Error fetching tracks", task.exception)
        }
    }

    return tracksLiveData
}
}