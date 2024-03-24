package com.example.playlistmaker.mediateka.ui.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistsViewModel : ViewModel() {
    //Screen state
    private val _screenState = MutableLiveData<PlaylistsScreenState>(PlaylistsScreenState.Loading)
    val screenState: LiveData<PlaylistsScreenState> = _screenState

    init {
        _screenState.value = PlaylistsScreenState.Content(emptyList())
    }

}