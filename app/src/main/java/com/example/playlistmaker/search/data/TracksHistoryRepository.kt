package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.models.Track

interface TracksHistoryRepository {
    fun saveToHistory(history: ArrayList<Track>)
    fun loadFromHistory(): ArrayList<Track>
    fun clearHistory()
}