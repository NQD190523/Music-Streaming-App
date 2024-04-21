package com.project.appealic.data.model

data class User(
    val name: String = "",
    val day_of_birth: String = "",
    val gender: String = "",
    val phone: String = "",
    val email: String = "",
    val likedSong: List<String> = listOf(),
    val likedPlaylist: List<String> = listOf(),
    val likedAlbum: List<String> = listOf(),
    val followArtist: List<String> = listOf()
)
