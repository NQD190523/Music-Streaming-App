package com.project.appealic.data.model

import com.project.appealic.data.model.Album
import com.project.appealic.data.model.ArtistX
import com.project.appealic.data.model.ExternalIds
import com.project.appealic.data.model.ExternalUrlsXXX

data class Track(
    val album: Album,
    val artists: List<ArtistX>,
    val available_markets: List<Any>,
    val disc_number: Int,
    val duration_ms: Int,
    val explicit: Boolean,
    val external_ids: ExternalIds,
    val external_urls: ExternalUrlsXXX,
    val href: String,
    val id: String,
    val is_local: Boolean,
    val name: String,
    val popularity: Int,
    val preview_url: Any,
    val track_number: Int,
    val type: String,
    val uri: String
)