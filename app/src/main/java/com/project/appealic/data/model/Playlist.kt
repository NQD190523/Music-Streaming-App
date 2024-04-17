package com.project.appealic.data.model

data class Playlist (
    val playlistArtists : String,
    val playlistId: String,
    val playlistName: String,
    val playlistThumb: String?,
    val trackIds : List<String>?
) {
    // Constructor không đối số
    constructor() : this("", "", "", null, null)
}