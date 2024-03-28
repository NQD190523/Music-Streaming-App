package com.project.appealic.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.spotify.protocol.types.Track
import kotlinx.coroutines.tasks.await

class SongRepository {
    val db = Firebase.firestore

    suspend fun getAllTrack() : List<Track> {
        try {
            val tracks = mutableListOf<Track>()
            val querySnapshot = db.collection("songs").get().await()
            for (document in querySnapshot) {
                val track = document.toObject(Track::class.java)
                track?.let {
                    tracks.add(it)
                }
            }
            return tracks
        } catch (e: Exception) {
             e.printStackTrace()
            return emptyList()
        }
    }
}