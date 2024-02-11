package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TracksHistoryRepository {
    fun saveToHistory(history: ArrayList<Track>)
    fun loadFromHistory(): ArrayList<Track>
    fun clearHistory()
}