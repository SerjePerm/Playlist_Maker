package com.example.playlistmaker.mediateka.domain.models

data class Playlist(
    val id: Int?,
    val title: String,
    val description: String?,
    val poster: String,
    val count: Int
)