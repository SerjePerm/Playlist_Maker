package com.example.playlistmaker.mediateka.domain.models

import android.net.Uri

data class Playlist(
    val id: Int?,
    val title: String,
    val description: String?,
    val poster: Uri?,
    val tracks: String,
    val count: Int
)