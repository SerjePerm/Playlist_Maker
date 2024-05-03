package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksResponse
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(
    private val repository: TracksRepository,
    private val history: TracksHistoryRepository
) : TracksInteractor {

    override fun searchTracks(expression: String): Flow<TracksResponse> {
        return repository.searchTracks(expression)
    }

    override fun saveToHistory(track: Track) {
        val array = loadFromHistory()
        if (array.contains(track)) array.remove(track)
        if (array.size > MAX_HISTORY_SIZE) array.removeAt(0)
        array.add(track)
        history.saveToHistory(array)
    }

    override fun loadFromHistory(): ArrayList<Track> {
        return history.loadFromHistory()
    }

    override fun clearHistory() {
        history.clearHistory()
    }

    companion object {
        private const val MAX_HISTORY_SIZE = 9
    }

}