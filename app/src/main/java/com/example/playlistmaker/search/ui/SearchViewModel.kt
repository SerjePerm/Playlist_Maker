package com.example.playlistmaker.search.ui

import android.app.Application
import android.os.Looper
import android.os.SystemClock
import androidx.core.util.Consumer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksResponse

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {

    //For debounce
    private val handler = android.os.Handler(Looper.getMainLooper())
    private var latestSearchText: String? = null
    //Screen state
    private val _screenState = MutableLiveData<SearchScreenState>(SearchScreenState.Loading)
    val screenState: LiveData<SearchScreenState> = _screenState

    init {
        loadAndSetSearchHistory()
    }

    private fun loadAndSetSearchHistory() {
        val searchHistory = tracksInteractor.loadFromHistory()
        _screenState.value = SearchScreenState.SearchHistory(searchHistory)
    }

    fun onSearchTextChanged(searchText: String) {
        //TODO() hide clear button
        if (searchText.isBlank()) loadAndSetSearchHistory()
        else searchDebounce(searchText)
    }

    fun searchDebounce(searchText: String) {
        if (latestSearchText == searchText) return
        latestSearchText = searchText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { searchRequest(searchText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(searchRunnable, SEARCH_REQUEST_TOKEN, postTime)
    }

    private fun searchRequest(searchText: String) {
        //TODO() set screen to loading
        val consumer = Consumer<TracksResponse> { tracksResponse ->
            if (tracksResponse.resultCode == 200) {
                if (tracksResponse.results.isNotEmpty()) {
                    _screenState.postValue(SearchScreenState.SearchResults(tracksResponse.results))
                }
                if (tracksResponse.results.isEmpty()) {
                    println("show empty results")
                }
            }
            else println("show no internet")
        }
        tracksInteractor.searchTracks(searchText, consumer)
    }

    fun onSearchTextFocusChanged(searchText: String, hasFocus: Boolean) {
        if (searchText.isEmpty() && hasFocus) {
            loadAndSetSearchHistory()
        }
    }

    fun clearSearchTextClick() {
        println("clearSearchTextClick")
    }

    fun clearHistoryClick() {
        tracksInteractor.clearHistory()
        loadAndSetSearchHistory()
    }

    fun saveToHistory(track: Track) {
        tracksInteractor.saveToHistory(track)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private  val SEARCH_REQUEST_TOKEN = Any()
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                SearchViewModel(
                    tracksInteractor = Creator.provideTracksInteractor(application)
                )
            }
        }
    }

}