package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.PlayerState

interface MediaPlayerData {
    fun getPlayerState(): PlayerState
    fun getPos(): Int
    fun play()
    fun pause()
    fun destroy()
}