package com.project.appealic.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithPlayLists(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "uid",
        entityColumn = "uid"
    )
    val playLists : List<PlayListEntity>?
)
