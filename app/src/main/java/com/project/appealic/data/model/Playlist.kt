package com.project.appealic.data.model

data class Playlist (
    val playlistId: String,
    val playlistName: String,
    val playlistThumb: String?,
    val trackIds : List<String>?
)