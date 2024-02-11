package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TracksResponse

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    fun saveToHistory(track: Track)
    fun loadFromHistory(): ArrayList<Track>
    fun clearHistory()

    interface TracksConsumer {
        fun consume(tracksResponse: TracksResponse)
    }
}