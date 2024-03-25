package com.example.playlistmaker.mediateka.ui.playlists

sealed class PlaylistsScreenState {
    data object Loading : PlaylistsScreenState()
    data class Content(val playlistsList: List<Any>) : PlaylistsScreenState()
    data object Error : PlaylistsScreenState()
}