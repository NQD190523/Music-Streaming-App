package com.project.appealic.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ProfileRepository {
    private val user = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()

    fun addUserToFirestore(user: FirebaseUser) {
        val userData = hashMapOf(
            "name" to "Nguyen Van A",
            "age" to 30,
            "email" to user.email
        )
        db.collection("users")
            .document(user.uid)
            .set(userData)

    }

}