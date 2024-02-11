package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.MediaPlayerData
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.models.PlayerState

class MediaPlayerInteractorImpl(private val mediaPlayer: MediaPlayerData) : MediaPlayerInteractor {
    override fun getState(): PlayerState {
        return mediaPlayer.playerState
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