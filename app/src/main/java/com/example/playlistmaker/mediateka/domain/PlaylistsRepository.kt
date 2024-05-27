package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.mediateka.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun playlists(): Flow<List<Playlist>>

    suspend fun getById(playlistId: Int): Playlist?
    suspend fun upsertPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlistId: Int)
}