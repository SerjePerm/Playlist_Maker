package com.example.playlistmaker.player.ui

import com.example.playlistmaker.player.domain.PlayerState

sealed class PlayerScreenState {
    data object Loading: PlayerScreenState()
    data class Content(
        val playerState: PlayerState = PlayerState.DEFAULT,
        val playerPos: Int = 0
    ): PlayerScreenState()
    data object Error: PlayerScreenState()
}