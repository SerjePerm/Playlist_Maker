package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.mediateka.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun playlists(): Flow<List<Playlist>>
    suspend fun upsertPlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
}