package com.project.appealic.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.appealic.data.model.PlayListEntity
import com.project.appealic.data.model.UserWithPlayLists
@Dao
interface PlayListDao {
    @Query("SELECT * FROM UserEntity WHERE uid = :uid")
    fun getUserPlayLists(uid : String) : UserWithPlayLists

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(playList: PlayListEntity)
}