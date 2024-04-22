package com.project.appealic.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.project.appealic.data.repository.AuthRepository

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _signInSuccess = MutableLiveData<Boolean>()
    val signInSuccess: LiveData<Boolean> = _signInSuccess

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    private val _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess: LiveData<Boolean> = _logoutSuccess

    constructor() : this(AuthRepository())

    private val auth = FirebaseAuth.getInstance()
    fun signInWithGoogle(account: GoogleSignInAccount) {
            repository.firebaseAuthWithGoogle(account)
                .addOnCompleteListener { task ->
                    _signInSuccess.value = task.isSuccessful
                    if(task.isSuccessful) Log.d("loginStatus", " Success")
                    else Log.e("LoginStatus", task.exception.toString())
                }

    }
    fun signInWithEmailAndPassword(email : String, password : String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                _signInSuccess.value = task.isSuccessful
                if (task.isSuccessful) Log.d("loginStatus", " Success")
                else Log.e("LoginStatus", task.exception.toString())
            }
    }
    fun loginWithEmailAndPassword(email: String, password: String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                _signInSuccess.value = task.isSuccessful
                if (task.isSuccessful) Log.d("loginStatus", " Success")
                else Log.e("LoginStatus", task.exception.toString())
            }
    }
    fun signOut(googleSignInClient: GoogleSignInClient) {
        FirebaseAuth.getInstance().signOut()
        googleSignInClient.signOut()
            .addOnCompleteListener {
                _logoutSuccess.postValue(true)
            }
    }

    fun autoSignIn(currentUser: FirebaseUser) {
        // Thực hiện đăng nhập tự động với currentUser
        _signInSuccess.postValue(true)
    }
    fun getUser(){
        _currentUser.value = repository.getUser()
    }
}