package com.project.appealic.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.project.appealic.data.model.Track
import com.project.appealic.data.model.User
import com.project.appealic.data.repository.AuthRepository
import com.project.appealic.data.repository.ProfileRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {
    private val user = FirebaseAuth.getInstance().currentUser
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = firebaseAuth.currentUser
    private val _userLiveData = MutableLiveData<User?>()
    val userLiveData: MutableLiveData<User?> = _userLiveData



    constructor() : this(ProfileRepository())

    fun createUserProfile(user: FirebaseUser, email: String) {
        profileRepository.addUserToFirestore(user, email)
    }

    fun createUserProfileByGoogle(account: GoogleSignInAccount, user: FirebaseUser) {
        profileRepository.addUserLoginByGoogleToFireStore(account, user)
    }

    fun getUserInfo(userId: String): User? {
        return profileRepository.getUserInfo(userId)
    }

    fun getCurrentUser(): User {
        val currentUser = firebaseAuth.currentUser
        return User(
            name = currentUser?.displayName ?: "",
            email = currentUser?.email ?: ""
        )
    }

    fun getUserProfile(uid: String) {
        viewModelScope.launch {
            try {
                val user = getUserProfileSuspend(uid)
                _userLiveData.postValue(user)
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error getting user profile: $e")
            }
        }
    }

    private suspend fun getUserProfileSuspend(uid: String): User? {
        val docRef = firestore.collection("users").document(uid)
        val document = docRef.get().await()
        return document.toObject(User::class.java)
    }
    fun updateUserName(uid: String, newName: String) {
        firestore.collection("users").document(uid)
            .update("name", newName)
            .addOnSuccessListener {
                // Update successful, reload user profile
                this.getUserProfile(uid)
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileViewModel", "Error updating user name: $exception")
            }
    }

    fun updateUserEmail(uid: String, newEmail: String) {
        firestore.collection("users").document(uid)
            .update("email", newEmail)
            .addOnSuccessListener {
                // Update successful, reload user profile
                getUserProfile(uid)
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileViewModel", "Error updating user email: $exception")
            }
    }

    fun updateUserPhone(uid: String, newPhone: String) {
        firestore.collection("users").document(uid)
            .update("phone", newPhone)
            .addOnSuccessListener {
                // Update successful, reload user profile
                getUserProfile(uid)
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileViewModel", "Error updating user phone: $exception")
            }
    }

    fun updateUserDOB(uid: String, newDOB: String) {
        firestore.collection("users").document(uid)
            .update("day_of_birth", newDOB)
            .addOnSuccessListener {
                // Update successful, reload user profile
                getUserProfile(uid)
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileViewModel", "Error updating user DOB: $exception")
            }
    }

    fun updateUserGender(uid: String, newGender: String) {
        firestore.collection("users").document(uid)
            .update("gender", newGender)
            .addOnSuccessListener {
                // Update successful, reload user profile
                getUserProfile(uid)
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileViewModel", "Error updating user gender: $exception")
            }
    }
}
