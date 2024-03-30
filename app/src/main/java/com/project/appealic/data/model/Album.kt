package com.project.appealic.data.model

data class Album(
    val title: String,
    val album_type: String,
    val artists: List<Artist>,
    val id: String,
    val images: List<Image>,
    val name: String,
    val release_date: String,
    val total_tracks: Int,
    val type: String,
)