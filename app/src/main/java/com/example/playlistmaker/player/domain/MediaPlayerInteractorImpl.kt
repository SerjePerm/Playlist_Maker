package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.data.MediaPlayerData

class MediaPlayerInteractorImpl(private val mediaPlayer: MediaPlayerData) : MediaPlayerInteractor {
    override fun getState(): PlayerState {
        return mediaPlayer.getPlayerState()
    }

    override fun getPos(): Int {
        return mediaPlayer.getPos()
    }

    override fun play() {
        mediaPlayer.play()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun stop() {
        mediaPlayer.destroy()
    }

}