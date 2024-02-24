package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.models.Track

sealed class SearchScreenState {
    data object Loading: SearchScreenState()
    data class SearchResults(val resultsList: List<Track>): SearchScreenState()
    data object EmptyResults: SearchScreenState()
    data class SearchHistory(val historyList: List<Track>): SearchScreenState()
    data object ClearSearchHistory: SearchScreenState()
    data class Error(val error: String): SearchScreenState()
}