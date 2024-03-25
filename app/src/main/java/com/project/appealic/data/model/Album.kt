package com.project.appealic.data.model

data class Album(
    val album_type: String,
    val artists: List<ArtistX>,
    val available_markets: List<Any>,
    val external_urls: ExternalUrlsXXX,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val total_tracks: Int,
    val type: String,
    val uri: String
)