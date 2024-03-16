package com.project.appealic.data.repository

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.project.appealic.Activity_welcome
import com.project.appealic.data.model.User

class AuthRepository {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

   fun firebaseAuthWithGoogle(account: GoogleSignInAccount){
            val credential = GoogleAuthProvider.getCredential(account.idToken,null)
            auth.signInWithCredential(credential)
    }
    fun getUser() : User?{
        return auth.currentUser?.let {
            User(it.uid, it.displayName ?:"",it.email ?:"",it.photoUrl?.toString() ?:"")
        }
    }
}