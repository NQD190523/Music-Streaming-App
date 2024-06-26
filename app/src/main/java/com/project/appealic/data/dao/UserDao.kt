package com.project.appealic.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.appealic.data.model.UserEntity
import com.project.appealic.data.model.UserWithPlayLists

@Dao
interface UserDao {
    @Query("SELECT * FROM userentity WHERE uid = :uid ")
    fun getUserData(uid : String) : LiveData<UserEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user : UserEntity )
}
