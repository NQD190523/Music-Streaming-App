package com.project.appealic.data.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.spotify.protocol.types.Track
import kotlinx.coroutines.tasks.await
import okhttp3.Callback

class SongRepository {
    val db = Firebase.firestore

    fun getAllTrack(): Task<QuerySnapshot> {
        return db.collection("tracks").get()

    }

    fun getAllArtist(): Task<QuerySnapshot>{
        return db.collection("artists").get()
    }
}