package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.mediateka.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
) : PlaylistsInteractor {

    override fun playlists(): Flow<List<Playlist>> {
        return playlistsRepository.playlists()
    }

    override fun getById(playlistId: Int): Flow<Playlist> {
        return  playlistsRepository.getById(playlistId)
    }

    override suspend fun upsertPlaylist(playlist: Playlist) {
        playlistsRepository.upsertPlaylist(playlist)
    }

    override suspend fun delPlaylist(playlist: Playlist) {
        playlistsRepository.deletePlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistsRepository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun delTrackFromPlaylist(track: Track, playlist: Playlist) {
        playlistsRepository.delTrackFromPlaylist(track, playlist)
    }

    override suspend fun getTrackById(trackId: Int): Track {
        return playlistsRepository.getTrackById(trackId)
    }

}