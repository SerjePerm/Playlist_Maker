package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.mediateka.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
) : PlaylistsInteractor {

    override fun playlists(): Flow<List<Playlist>> {
        return playlistsRepository.playlists()
    }

    override suspend fun upsertPlaylist(playlist: Playlist) {
        playlistsRepository.upsertPlaylist(playlist)
    }

}