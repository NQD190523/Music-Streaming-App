package com.project.appealic.data.repository

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.project.appealic.data.model.Album

class AlbumRepository(application: Application) {
    val firebaseDB = Firebase.firestore

    fun getAllAlbums(): Task<QuerySnapshot> {
        return firebaseDB.collection("albums").get()
    }
    fun getTracksFromAlbum(albumId : String) : Task<DocumentSnapshot>{
        return  firebaseDB.collection("albums").document(albumId).get()
    }
    fun loadAlbumSearchResults(searchQuery: String?): LiveData<List<Album>> {
        val searchResultsLiveData = MutableLiveData<List<Album>>()
        if (searchQuery.isNullOrEmpty()) {
            Log.d(TAG, "loadAlbumSearchResults: No search query provided")
            return searchResultsLiveData
        }

        val albumTask = firebaseDB.collection("albums").get()
        albumTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val albumsSnapshot = task.result
                val albums = albumsSnapshot?.documents?.mapNotNull { it.toObject(Album::class.java) } ?: emptyList()
                val filteredAlbums = albums.filter { it.title?.contains(searchQuery, ignoreCase = true) == true }
                searchResultsLiveData.postValue(filteredAlbums)
            } else {
                Log.e(TAG, "loadAlbumSearchResults: Error fetching data", task.exception)
            }
        }

        return searchResultsLiveData
    }
}