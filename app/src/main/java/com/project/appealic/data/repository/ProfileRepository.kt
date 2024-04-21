package com.project.appealic.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.project.appealic.data.model.User


class ProfileRepository {
    private val user = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()

    fun addUserToFirestore(user: FirebaseUser, email : String) {
        val userData = User("","","","",email)
        db.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d("Firestore", "User document already exists")
                } else {
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
            }
    }

    fun addUserLoginByGoogleToFireStore(
        account: GoogleSignInAccount,
        user: FirebaseUser
    ) {
        try {
            val userData = User(account.displayName!!, "", "", "", account.email!!)
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

    fun getUserInfo(userId: String): User? {
        val task = FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .get()

        while (!task.isComplete) {
            // Chờ cho đến khi nhiệm vụ hoàn thành
        }
        return if (task.isSuccessful) {
            val documentSnapshot: DocumentSnapshot? = task.result
            documentSnapshot?.toObject(User::class.java)
        } else {
            null
        }
    }
}