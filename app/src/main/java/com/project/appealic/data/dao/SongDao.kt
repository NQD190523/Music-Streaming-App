package com.project.appealic.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.appealic.data.model.SongEntity
import com.project.appealic.data.model.Track

@Dao
interface SongDao {
    @Query("SELECT * FROM songentity WHERE songId = :songId")
    fun getSong(songId: String): SongEntity?
    @Query("SELECT * FROM songentity WHERE userId = :userId ORDER BY listenedAt DESC")
    fun getRecentSongs(userId: String): LiveData<List<SongEntity>>

    @Query("SELECT * FROM songentity WHERE userId = :userId AND liked == true")
    fun getLikedSongs(userId: String) : LiveData<List<SongEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSong(song: SongEntity)
}