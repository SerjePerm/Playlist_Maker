package com.example.playlistmaker.mediateka.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoritesViewModel : ViewModel() {
    //Screen state
    private val _screenState = MutableLiveData<FavoritesScreenState>(FavoritesScreenState.Loading)
    val screenState: LiveData<FavoritesScreenState> = _screenState

    init {
        _screenState.value = FavoritesScreenState.Content(emptyList())
    }

}