package com.example.playlistmaker.presentation.presenters.mediaplayer

import com.example.playlistmaker.domain.models.PlayerState

interface MediaPlayerView {
    fun setState(playerState: PlayerState)
}