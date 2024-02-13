package com.example.playlistmaker.domain.api

import androidx.core.util.Consumer
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TracksResponse

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: Consumer<TracksResponse>)
    fun saveToHistory(track: Track)
    fun loadFromHistory(): ArrayList<Track>
    fun clearHistory()
}