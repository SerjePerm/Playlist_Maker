package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {

    //For debounce
    private var latestSearchText: String? = null
    private val trackSearchDebounce = debounce<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY_MILLIS,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { changedText ->
        searchRequest(changedText)
    }

    //Screen state
    private val _screenState = MutableLiveData<SearchScreenState>(SearchScreenState.Loading)
    val screenState: LiveData<SearchScreenState> = _screenState

    init {
        loadAndSetSearchHistory()
    }

    fun loadAndSetSearchHistory() {
        val searchHistory = tracksInteractor.loadFromHistory()
        _screenState.value = SearchScreenState.SearchHistory(searchHistory)
    }

    fun searchDebounce(searchText: String) {
        if (searchText.isBlank()) return
        if (latestSearchText == searchText) return
        latestSearchText = searchText
        trackSearchDebounce(searchText)
    }

    fun searchRequest(searchText: String) {
        _screenState.value = SearchScreenState.Loading
        viewModelScope.launch {
            tracksInteractor.searchTracks(searchText).collect { response ->
                if (response.resultCode == 200) {
                    _screenState.postValue(SearchScreenState.SearchResults(response.results))
                } else _screenState.postValue(SearchScreenState.Error)
            }
        }
    }

    fun clearHistoryClick() {
        tracksInteractor.clearHistory()
        loadAndSetSearchHistory()
    }

    fun saveToHistory(track: Track) {
        tracksInteractor.saveToHistory(track)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }

}