package com.project.appealic.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.app.Application
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.project.appealic.data.model.Artist

class ArtistRepository(application: Application) {
    val firebaseDB = Firebase.firestore

    fun getAllArtist(): Task<QuerySnapshot> {
        return firebaseDB.collection("artists").get()
    }
    fun getTrackFromArtist(artistId : String): Task<QuerySnapshot> {
        return firebaseDB.collection("tracks")
            .whereEqualTo("artistId", artistId)
            .get()
    }
    fun getFollowArtistFromUser(userId: String): Task<DocumentSnapshot> {
        return firebaseDB.collection("users").document(userId)
            .get()
    }

    fun addArtistToUserFollowArtist(userId: String, artistId: String) {
        val userDocRef = firebaseDB.collection("users").document(userId)
        // Cập nhật tài liệu người dùng với ID bài hát mới
        userDocRef.update("followArtist", FieldValue.arrayUnion(artistId))
            .addOnSuccessListener {
                println("Artist ID $artistId added to user $userId liked songs successfully")
            }
            .addOnFailureListener { exception ->
                println("Error adding track ID to user liked songs: $exception")
            }
    }
    fun removeArtistToUserFollowArtist(userId: String, artistId: String) {
        val userDocRef = firebaseDB.collection("users").document(userId)

        // Cập nhật tài liệu người dùng, xóa trackId khỏi mảng likedSongs
        userDocRef.update("followArtist", FieldValue.arrayRemove(artistId))
            .addOnSuccessListener {
                println("Artist ID $artistId removed from user $userId liked songs successfully")
            }
            .addOnFailureListener { exception ->
                println("Error removing track ID from user liked songs: $exception")
            }
    }

    fun loadArtistSearchResults(searchQuery: String?): LiveData<List<Artist>> {
        val searchResultsLiveData = MutableLiveData<List<Artist>>()
        if (searchQuery.isNullOrEmpty()) {
            Log.d(TAG, "loadArtistSearchResults: No search query provided")
            return searchResultsLiveData
        }

        val artistsTask = firebaseDB.collection("artists").get()
        artistsTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val artistsSnapshot = task.result
                val artists = artistsSnapshot?.documents?.mapNotNull { it.toObject(Artist::class.java) } ?: emptyList()
                val filteredArtists = artists.filter { it.Name?.contains(searchQuery, ignoreCase = true) == true }
                searchResultsLiveData.postValue(filteredArtists)
            } else {
                Log.e(TAG, "loadArtistSearchResults: Error fetching data", task.exception)
            }
        }

        return searchResultsLiveData
    }

}
