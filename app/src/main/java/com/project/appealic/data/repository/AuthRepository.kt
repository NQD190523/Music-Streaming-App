package com.project.appealic.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class AuthRepository {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

   fun firebaseAuthWithGoogle(account: GoogleSignInAccount) : Task<AuthResult> {
            val credential = GoogleAuthProvider.getCredential(account.idToken,null)
            return auth.signInWithCredential(credential)
   }
    fun getUser() : FirebaseUser?{
        return auth.currentUser
    }
    fun signOut(googleSignInClient: GoogleSignInClient) : LiveData<Boolean>{
        val logoutSuccess = MutableLiveData<Boolean>()
        googleSignInClient.signOut().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                logoutSuccess.value = true
            } else {
                logoutSuccess.value = false
            }
        }
        return logoutSuccess
    }
}