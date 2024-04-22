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
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
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
import com.project.appealic.data.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.Callback
import java.util.concurrent.CompletableFuture

class SongRepository (application: Application) {

    val firebaseDB = Firebase.firestore

    private val db: AppDatabase = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java, "appealic"
    )
        .fallbackToDestructiveMigration()
        .build()
    private val songDao: SongDao = db.songDao()


    fun getAllTrack(): Task<QuerySnapshot> {
        return firebaseDB.collection("tracks").get()
    }


    fun getAllArtist(): Task<QuerySnapshot> {
        return firebaseDB.collection("artists").get()
    }

    fun getAllPlaylists(): Task<QuerySnapshot> {
        return firebaseDB.collection("playlists").get()
    }

    fun getRecentSongs(userId: String): LiveData<List<SongEntity>> {
        return songDao.getRecentSongs(userId)
    }

    fun getTrackByUrl(trackUrl : String) : Task<QuerySnapshot>{
        return firebaseDB.collection("tracks")
            .whereEqualTo("trackUrl", trackUrl)
            .get()
            .addOnSuccessListener {
                    Log.d("FirestoreData", "Success")
                }
            .addOnFailureListener { exception ->
                // Xử lý khi có lỗi xảy ra
                Log.e("FirestoreData", "Error getting documents: $exception")
            }
    }
    fun getLikedSongFromUser(userId: String): Task<DocumentSnapshot> {
        return firebaseDB.collection("users").document(userId)
            .get()
    }
    fun addTrackToUserLikedSongs(userId: String, trackId: String) {
        val userDocRef = firebaseDB.collection("users").document(userId)
        // Cập nhật tài liệu người dùng với ID bài hát mới
        userDocRef.update("likedSong", FieldValue.arrayUnion(trackId))
            .addOnSuccessListener {
                println("Track ID $trackId added to user $userId liked songs successfully")
            }
            .addOnFailureListener { exception ->
                println("Error adding track ID to user liked songs: $exception")
            }
    }
    fun removeTrackFromUserLikedSongs(userId: String, trackId: String) {
        val userDocRef = firebaseDB.collection("users").document(userId)

        // Cập nhật tài liệu người dùng, xóa trackId khỏi mảng likedSongs
        userDocRef.update("likedSong", FieldValue.arrayRemove(trackId))
            .addOnSuccessListener {
                println("Track ID $trackId removed from user $userId liked songs successfully")
            }
            .addOnFailureListener { exception ->
                println("Error removing track ID from user liked songs: $exception")
            }
    }

//    suspend fun getLikedSongFromUser(userId: String): List<Map<String, Any>> {
//        return withContext(Dispatchers.IO) {
//            val userDocRef = firebaseDB.collection("users").document(userId)
//            val userDoc = userDocRef.get().await()
//
//            if (userDoc.exists()) {
//                val favoriteSongIds = userDoc["likedSong"] as? List<String> ?: emptyList()
//
//                // Truy vấn các bài hát từ collection "tracks" dựa trên danh sách ID
//                val querySnapshot =
//                    firebaseDB.collection("tracks").whereIn(FieldPath.documentId(), favoriteSongIds)
//                        .get().await()
//                val tracksList = mutableListOf<Map<String, Any>>()
//
//                for (doc in querySnapshot.documents) {
//                    doc.data?.let { tracksList.add(it) }
//                }
//                tracksList
//            } else {
//                emptyList()
//            }
//        }
//    }

    fun insertSong(song: SongEntity) {
        songDao.insertSong(song)
    }

    fun loadSongSearchResults(searchQuery: String?): LiveData<List<Track>> {
        val searchResultsLiveData = MutableLiveData<List<Track>>()
        if (searchQuery.isNullOrEmpty()) {
            Log.d(TAG, "loadSongSearchResults: No search query provided")
            return searchResultsLiveData
        }
        val tracksTask = firebaseDB.collection("tracks").get()
        tracksTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val tracksSnapshot = task.result
                val tracks = tracksSnapshot?.documents?.mapNotNull { it.toObject(Track::class.java) } ?: emptyList()
                val filteredTracks = tracks.filter { it.trackTitle?.contains(searchQuery, ignoreCase = true) == true }
                searchResultsLiveData.postValue(filteredTracks)
            } else {
                Log.e(TAG, "loadSongSearchResults: Error fetching data", task.exception)
            }
        }

        return searchResultsLiveData
    }
}
