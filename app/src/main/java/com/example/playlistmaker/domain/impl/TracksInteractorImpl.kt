package com.example.playlistmaker.domain.impl

import androidx.core.util.Consumer
import com.example.playlistmaker.domain.api.TracksHistoryRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TracksResponse

class TracksInteractorImpl(
    private val repository: TracksRepository,
    private val history: TracksHistoryRepository
) : TracksInteractor {

    override fun searchTracks(expression: String, consumer: Consumer<TracksResponse>) {
        val t = Thread { consumer.accept(repository.searchTracks(expression)) }
        t.start()
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
        private const val MAX_HISTORY_SIZE = 0
    }

}