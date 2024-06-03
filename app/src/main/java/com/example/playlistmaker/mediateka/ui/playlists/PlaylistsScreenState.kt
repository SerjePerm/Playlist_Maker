package com.example.playlistmaker.mediateka.ui.playlists

import com.example.playlistmaker.mediateka.domain.models.Playlist

sealed class PlaylistsScreenState {
    data object Loading : PlaylistsScreenState()
    data class Content(val playlistsList: List<Playlist>) : PlaylistsScreenState()
    data object Error : PlaylistsScreenState()
}