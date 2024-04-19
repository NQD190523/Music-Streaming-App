package com.project.appealic.data.model

data class SearchResults (
    val tracks: List<Track>,
    val artist: List<Artist>,
    val playlists: List<Playlist>,
    val albums: List<Album>
)