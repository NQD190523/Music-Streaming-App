package com.project.appealic.data.repository

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
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
import com.project.appealic.data.model.SearchResults
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

    fun getAllPlaylists(): Task<QuerySnapshot> {
        return firebaseDB.collection("playlists").get()
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
fun loadSearchResults(searchQuery: String?): LiveData<SearchResults> {
    val searchResultsLiveData = MutableLiveData<SearchResults>()
    if (searchQuery.isNullOrEmpty()) {
        Log.d(TAG, "loadSearchResults: No search query provided")
        return searchResultsLiveData
    }

    val tracksTask = firebaseDB.collection("tracks").get()
    val artistsTask = firebaseDB.collection("artists").get()

    Tasks.whenAllComplete(tracksTask, artistsTask).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val tasks = task.result
            var tracksSnapshot: QuerySnapshot? = null
            var artistsSnapshot: QuerySnapshot? = null

            for (t in tasks) {
                if (t.isSuccessful) {
                    val result = t.result
                    if (result is QuerySnapshot) {
                        if (tracksSnapshot == null) {
                            tracksSnapshot = result
                        } else if (artistsSnapshot == null) {
                            artistsSnapshot = result
                        }
                    }
                } else {
                    Log.e(TAG, "loadSearchResults: Error fetching data", t.exception)
                }
            }

            if (tracksSnapshot != null || artistsSnapshot != null) {
                val tracks = tracksSnapshot?.documents?.mapNotNull { it.toObject(com.project.appealic.data.model.Track::class.java) } ?: emptyList()
                val artists = artistsSnapshot?.documents?.mapNotNull { it.toObject(com.project.appealic.data.model.Artist::class.java) } ?: emptyList()

                val filteredTracks = tracks.filter { it.trackTitle?.contains(searchQuery, ignoreCase = true) == true }
                val filteredArtists = artists.filter { it.Name?.contains(searchQuery, ignoreCase = true) == true }

                searchResultsLiveData.postValue(SearchResults(filteredTracks, filteredArtists))
            } else {
                Log.e(TAG, "loadSearchResults: No data found")
            }
        } else {
            Log.e(TAG, "loadSearchResults: Error fetching data", task.exception)
        }
    }

    return searchResultsLiveData
}
}