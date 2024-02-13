package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.PlayerState

interface MediaPlayerInteractor {

    fun getState(): PlayerState
    fun getPos(): Int

    fun play()
    fun pause()
    fun stop()
}