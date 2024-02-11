package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.PlayerState

interface MediaPlayerData {

    var playerState: PlayerState

    fun getPos(): Int
    fun play()
    fun pause()
    fun destroy()
}