package com.example.playlistmaker.mediateka.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.domain.FavoritesInteractor
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {
    //Screen state
    private val _screenState = MutableLiveData<FavoritesScreenState>(FavoritesScreenState.Loading)
    val screenState: LiveData<FavoritesScreenState> = _screenState

    init {
        viewModelScope.launch {
            favoritesInteractor.favoriteTracks().collect { trackList ->
                _screenState.value = FavoritesScreenState.Content(trackList)
            }
        }
    }

}