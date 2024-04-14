package com.project.appealic.data.model

import androidx.lifecycle.LiveData
import androidx.room.Embedded
import androidx.room.Relation

data class PlayListWithSongsEntity(
    @Embedded val playListEntity: PlayListEntity,
    @Relation(
        parentColumn = "playListId",
        entityColumn = "songId"
    )
    val songs : LiveData<List<SongEntity>>
)
