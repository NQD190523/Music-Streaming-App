package com.project.appealic.ui.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.project.appealic.data.model.Artist
import com.project.appealic.data.model.Track
import com.project.appealic.data.repository.ArtistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistViewModel(private val artistRepository: ArtistRepository): ViewModel() {

    private val firebaseDB = Firebase.firestore
    private val _likedArtist = MutableLiveData<List<Artist>>()
    val likedArtist: LiveData<List<Artist>> get() = _likedArtist

    fun getLikedSongs(userId: String){
        artistRepository.getFollowArtistFromUser(userId)
            .addOnSuccessListener { userDoc ->
                if (userDoc.exists()) {
                    val favoriteSongIds = userDoc["followArtist"] as? List<String> ?: emptyList()

                    val tasks = favoriteSongIds.map { trackId ->
                        val trackDocRef = firebaseDB.collection("tracks").document(trackId)
                        trackDocRef.get().continueWith { task ->
                            if (task.isSuccessful) {
                                task.result?.toObject(Artist::class.java)
                            } else {
                                null
                            }
                        }
                    }
                    Tasks.whenAllSuccess<Artist?>(tasks).addOnSuccessListener { documents ->
                        val tracksList = documents.filterNotNull()
                        // Xử lý danh sách trackList ở đây
                        _likedArtist.postValue(tracksList)
                    }.addOnFailureListener { exception ->
                        Log.e(ContentValues.TAG, "Error fetching liked songs: $exception")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error fetching user document: $exception")
            }
    }
    fun addArtistToUserFollowArtist ( userId: String, trackId : String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            artistRepository.addArtistToUserFollowArtist(userId, trackId)
        }
    }

    fun  removeArtistToUserFollowArtist( userId: String, trackId : String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            artistRepository.removeArtistToUserFollowArtist(userId, trackId)
        }
    }
}