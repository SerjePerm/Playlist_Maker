package com.example.playlistmaker.search.domain

import androidx.core.util.Consumer
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksResponse

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: Consumer<TracksResponse>)
    fun saveToHistory(track: Track)
    fun loadFromHistory(): ArrayList<Track>
    fun clearHistory()
}