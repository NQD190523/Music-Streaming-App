package com.project.appealic.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.project.appealic.data.model.User
import com.project.appealic.data.repository.ProfileRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val _userLiveData = MutableLiveData<User?>()
    val userLiveData: LiveData<User?> = _userLiveData

    private val _updatePhoneLiveData = MutableLiveData<String?>()
    val updatePhoneLiveData: LiveData<String?> = _updatePhoneLiveData

    private val _updateNameLiveData = MutableLiveData<String?>()
    val updateNameLiveData: LiveData<String?> = _updateNameLiveData

    private val _updateEmailLiveData = MutableLiveData<String?>()
    val updateEmailLiveData: LiveData<String?> = _updateEmailLiveData

    private val _updateDOBLiveData = MutableLiveData<String?>()
    val updateDOBLiveData: LiveData<String?> = _updateDOBLiveData

    private val _updateGenderLiveData = MutableLiveData<String?>()
    val updateGenderLiveData: LiveData<String?> = _updateGenderLiveData

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

    fun getCurrentUser(): User? {
        val currentUser = firebaseAuth.currentUser
        return currentUser?.let {
            User(
                name = it.displayName ?: "",
                email = it.email ?: ""
            )
        }
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

    fun updateUserProfile(uid: String, updatedUser: String) {
        firestore.collection("users").document(uid)
            .set(updatedUser)
            .addOnSuccessListener {
                getUserProfile(uid)
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileViewModel", "Error updating user profile: $exception")
            }
    }

    fun updateUserPhone(uid: String, newPhone: String) {
        firestore.collection("users").document(uid)
            .update("phone", newPhone)
            .addOnSuccessListener {
                _updatePhoneLiveData.postValue(newPhone)
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileViewModel", "Error updating user phone: $exception")
                _updatePhoneLiveData.postValue(null)
            }
    }

    fun updateUserName(uid: String, newName: String) {
        firestore.collection("users").document(uid)
            .update("name", newName)
            .addOnSuccessListener {
                _updateNameLiveData.postValue(newName)
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileViewModel", "Error updating user name: $exception")
                _updateNameLiveData.postValue(null)
            }
    }

    fun updateUserEmail(uid: String, newEmail: String) {
        firestore.collection("users").document(uid)
            .update("email", newEmail)
            .addOnSuccessListener {
                _updateEmailLiveData.postValue(newEmail)
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileViewModel", "Error updating user email: $exception")
                _updateEmailLiveData.postValue(null)
            }
    }

    fun updateUserDOB(uid: String, newDOB: String) {
        firestore.collection("users").document(uid)
            .update("day_of_birth", newDOB)
            .addOnSuccessListener {
                _updateDOBLiveData.postValue(newDOB)
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileViewModel", "Error updating user DOB: $exception")
                _updateDOBLiveData.postValue(null)
            }
    }

    fun updateUserGender(uid: String, newGender: String) {
        firestore.collection("users").document(uid)
            .update("gender", newGender)
            .addOnSuccessListener {
                _updateGenderLiveData.postValue(newGender)
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileViewModel", "Error updating user gender: $exception")
                _updateGenderLiveData.postValue(null)
            }
    }
}