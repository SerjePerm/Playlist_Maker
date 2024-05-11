package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
) : FavoritesInteractor {

    override fun favoriteTracks(): Flow<List<Track>> {
        return favoritesRepository.favoriteTracks()
    }

    override fun favoriteIds(): Flow<List<Int>> {
        return favoritesRepository.favoriteIds()
    }

    override suspend fun changeFavorite(track: Track) {
        if (favoritesRepository.getById(track.trackId) == null) {
            favoritesRepository.upsertTrack(track)
        } else favoritesRepository.deleteTrack(track.trackId)
    }

}