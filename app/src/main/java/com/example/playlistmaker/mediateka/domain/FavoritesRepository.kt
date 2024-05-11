package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun favoriteTracks(): Flow<List<Track>>
    fun favoriteIds(): Flow<List<Int>>
    suspend fun getById(trackId: Int): Track?
    suspend fun upsertTrack(track: Track)
    suspend fun deleteTrack(trackId: Int)
}