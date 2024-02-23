package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.models.TracksResponse

interface TracksRepository {
    fun searchTracks(expression: String): TracksResponse
}