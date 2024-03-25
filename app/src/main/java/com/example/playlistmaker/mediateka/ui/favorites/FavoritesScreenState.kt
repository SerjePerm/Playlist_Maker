package com.example.playlistmaker.mediateka.ui.favorites

import com.example.playlistmaker.search.domain.models.Track

sealed class FavoritesScreenState {
    data object Loading : FavoritesScreenState()
    data class Content(val favoritesList: List<Track>) : FavoritesScreenState()
    data object Error : FavoritesScreenState()
}