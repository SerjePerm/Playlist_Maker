package com.example.playlistmaker.player.domain

interface MediaPlayerInteractor {
    fun setDataSource(url: String)
    fun getState(): PlayerState
    fun getPos(): Int
    fun play()
    fun pause()
    fun stop()
}