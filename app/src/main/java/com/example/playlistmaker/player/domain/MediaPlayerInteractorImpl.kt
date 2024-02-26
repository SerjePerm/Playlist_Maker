package com.example.playlistmaker.player.domain

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