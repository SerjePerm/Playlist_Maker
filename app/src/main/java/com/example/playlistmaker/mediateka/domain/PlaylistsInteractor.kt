package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.mediateka.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun playlists(): Flow<List<Playlist>>

    suspend fun upsertPlaylist(playlist: Playlist)
}