package com.example.playlistmaker.player.domain

interface MediaPlayerInteractor {

    fun getState(): PlayerState
    fun getPos(): Int

    fun play()
    fun pause()
    fun stop()
}