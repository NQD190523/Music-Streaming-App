package com.project.appealic.data.model

import java.util.Date

data class User(
    val name: String = "",
    val day_of_birth : String = "",
    val gender : String = "",
    val phone : Int = 0,
    val email: String = "",
    val likedSong : List<String> = listOf(),
    val likedPlaylist: List<String> = listOf(),
    val likedAlbum : List<String> = listOf(),
    val followArtist : List<String> = listOf()
)
