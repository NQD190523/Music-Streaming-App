package com.project.appealic.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.project.appealic.data.model.User


class ProfileRepository {
    private val user = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()

    fun addUserToFirestore(user: FirebaseUser, email : String) {
        val userData = User("","","",99999999,email)
        db.collection("users")
            .document(user.uid)
            .set(userData)
            .addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot added with ID: user1")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding document", e)
            }
    }

    fun addUserLoginByGoogleToFireStore(
        account: GoogleSignInAccount,
        user: FirebaseUser
    ) {
        try {
            val userData = User(account.displayName!!, "", "", 9999999, account.email!!)
            db.collection("users")
                .document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        Log.d("Firestore", "User document already exists")
                    }else {
                        // Nếu tài liệu chưa tồn tại, bạn có thể tạo một tài liệu mới
                        db.collection("users")
                            .document(user.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                Log.d("Firestore", "DocumentSnapshot added with ID: ${user.uid}")
                            }
                            .addOnFailureListener { e ->
                                Log.e("Firestore", "Error adding document", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error getting document", e)
                }
        } catch (e: ApiException) {
            Log.e(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }
}