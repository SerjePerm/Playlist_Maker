package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.domain.PlayerState

interface MediaPlayerData {
    fun getPlayerState(): PlayerState
    fun getPos(): Int
    fun play()
    fun pause()
    fun destroy()
}