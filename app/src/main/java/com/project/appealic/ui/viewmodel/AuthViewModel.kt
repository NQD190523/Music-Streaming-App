package com.project.appealic.ui.viewmodel

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.project.appealic.Activity_welcome
import com.project.appealic.data.model.User
import com.project.appealic.data.repository.AuthRepository
import com.project.appealic.ui.view.GoogleLoginActivity
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _signInSuccess = MutableLiveData<Boolean>()

    val signInSuccess: LiveData<Boolean> = _signInSuccess

    constructor() : this(AuthRepository())

    private val auth = FirebaseAuth.getInstance()
    fun signInWithGoogle(account: GoogleSignInAccount) = viewModelScope.launch {
            repository.firebaseAuthWithGoogle(account)

    }
    fun getUser() : User? {
        return repository.getUser()
    }

}