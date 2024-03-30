package com.project.appealic.data.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.spotify.protocol.types.Track
import kotlinx.coroutines.tasks.await
import okhttp3.Callback

class SongRepository {
    val db = Firebase.firestore

    fun getAllTrack( callback:(List<com.project.appealic.data.model.Track>) ->Unit) {
        db.collection("songs").get()
            .addOnSuccessListener { result ->
                val tracks = mutableListOf<com.project.appealic.data.model.Track>()
                for (document in result) {
                    val track = document.toObject(com.project.appealic.data.model.Track::class.java)
                    tracks.add(track)
                }
                callback(tracks)
            }
            .addOnFailureListener {exeption ->
                Log.e("error", exeption.toString())
            }
    }

    fun getArtist(artistId : String): Task<DocumentSnapshot>{
        return db.collection("artists").document(artistId).get()
    }
}