package com.example.playlistmaker.player.domain

interface MediaPlayerData {
    fun setDataSource(url: String)
    fun getPlayerState(): PlayerState
    fun getPos(): Int
    fun play()
    fun pause()
    fun destroy()
}