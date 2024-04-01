package com.project.appealic.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.project.appealic.data.dao.UserDao
import com.project.appealic.data.database.AppDatabase
import com.project.appealic.data.model.UserEntity

class UserRepository(application: Application) {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db: AppDatabase = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java,"appealic"
    ).build()
    private val userDao : UserDao = db.userDao()


    fun getUserData(): LiveData<UserEntity>? {
        val userId = auth.currentUser?.uid
        return userId?.let { userDao.getUserData(it) }
    }

    suspend fun insert(user : UserEntity){
        userDao.insert(user)
    }
}