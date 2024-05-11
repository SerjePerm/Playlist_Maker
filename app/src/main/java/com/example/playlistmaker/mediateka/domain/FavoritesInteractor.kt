package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun favoriteTracks(): Flow<List<Track>>
    fun favoriteIds(): Flow<List<Int>>
    suspend fun changeFavorite(track: Track)
}