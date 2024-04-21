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
import com.project.appealic.data.model.PlayListEntity
import com.project.appealic.data.model.Playlist
import com.project.appealic.data.model.Track

class PlayListRepository(application: Application) {
    private val firebaseDB = Firebase.firestore

    fun createNewPlaylist(playlist: PlayListEntity) {
        val playlistRef = firebaseDB.collection("playlists").document()
        playlist.playlistId = playlistRef.id
        playlistRef.set(playlist)
            .addOnSuccessListener { Log.d("PlayListRepository", "Playlist created successfully") }
            .addOnFailureListener { e -> Log.w("PlayListRepository", "Error creating playlist", e) }
    }

    fun getAllUserPlayList(userId: String): MutableLiveData<List<PlayListEntity>?> {
        val playlistsLiveData = MutableLiveData<List<PlayListEntity>?>()
        firebaseDB.collection("playlists")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("PlayListRepository", "Error listening for playlists", e)
                    return@addSnapshotListener
                }

                val playlists = snapshot?.toObjects(PlayListEntity::class.java)
                playlistsLiveData.value = playlists
            }
        return playlistsLiveData
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

    fun addTrackToPlaylist(track: Track, playlist: PlayListEntity) {
        // Implement adding a track to a playlist in Firestore
        val playlistRef = firebaseDB.collection("playlists").document(playlist.playlistId)
        val trackIds = playlist.trackIds.toMutableList()
        trackIds.add(track.trackId.toString())
        playlistRef.update("trackIds", trackIds)
            .addOnSuccessListener {
                Log.d("PlayListRepository", "Track added to playlist successfully")
            }
            .addOnFailureListener { e ->
                Log.w("PlayListRepository", "Error adding track to playlist", e)
            }
    }
}