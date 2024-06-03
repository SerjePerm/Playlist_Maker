package com.example.playlistmaker.mediateka.ui.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.domain.PlaylistsInteractor
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {
    //Screen state
    private val _screenState = MutableLiveData<PlaylistsScreenState>(PlaylistsScreenState.Loading)
    val screenState: LiveData<PlaylistsScreenState> = _screenState

    init {
        viewModelScope.launch {
            playlistsInteractor.playlists().collect { list ->
                _screenState.value = PlaylistsScreenState.Content(list)
            }
        }
    }

}