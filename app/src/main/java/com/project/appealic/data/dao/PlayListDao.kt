
package com.project.appealic.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.project.appealic.data.model.PlayListEntity

@Dao
interface PlayListDao {
    @Query("SELECT * FROM playlists WHERE userId = :userId")
    fun getUserPlayLists(userId: String): List<PlayListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(playList: PlayListEntity)

    @Update
    fun update(playList: PlayListEntity)
}
