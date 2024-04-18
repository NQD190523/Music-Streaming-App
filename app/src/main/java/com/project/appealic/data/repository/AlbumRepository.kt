package com.project.appealic.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore

class AlbumRepository {
    val firebaseDB = Firebase.firestore

    fun getAllAlbums(): Task<QuerySnapshot> {
        return firebaseDB.collection("albums").get()
    }
    fun getTracksFromAlbum(albumId : String) : Task<DocumentSnapshot>{
        return  firebaseDB.collection("albums").document(albumId).get()
    }

}