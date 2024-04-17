package com.project.appealic.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.project.appealic.data.model.Track
import com.project.appealic.data.model.User
import com.project.appealic.data.repository.AuthRepository
import com.project.appealic.data.repository.ProfileRepository

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {
    private val user = FirebaseAuth.getInstance().currentUser

    constructor() : this(ProfileRepository())
    fun createUserProfile(user: FirebaseUser, email : String){
        profileRepository.addUserToFirestore(user,email)
    }

    fun createUserProfileByGoogle(account: GoogleSignInAccount, user: FirebaseUser){
        profileRepository.addUserLoginByGoogleToFireStore(account,user)
    }

}