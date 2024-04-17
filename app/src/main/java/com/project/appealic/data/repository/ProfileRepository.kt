package com.project.appealic.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileRepository {
    private val user = FirebaseAuth.getInstance().currentUser
    val firebaseDatabase = Firebase.database



}