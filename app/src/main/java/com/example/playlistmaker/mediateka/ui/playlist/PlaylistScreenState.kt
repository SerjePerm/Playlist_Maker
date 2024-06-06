package com.example.playlistmaker.mediateka.ui.playlist

import com.example.playlistmaker.mediateka.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track

data class PlaylistScreenState(
    val playlist: Playlist = Playlist(null, "", null, null, "", 0),
    val trackList: List<Track> = emptyList(),
    val tracksLength: Int = 0
)