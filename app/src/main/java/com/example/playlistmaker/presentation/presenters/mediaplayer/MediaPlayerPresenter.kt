package com.example.playlistmaker.presentation.presenters.mediaplayer

import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.models.PlayerState

class MediaPlayerPresenter(
    private val view: MediaPlayerView,
    private val mediaPlayerInteractor: MediaPlayerInteractor
) {

    private fun play() {
        mediaPlayerInteractor.play()
        view.setState(PlayerState.PLAYING)
    }

    private fun pause() {
        mediaPlayerInteractor.pause()
        view.setState(PlayerState.PAUSED)
    }

    fun playPauseClick() {
        if (mediaPlayerInteractor.getState() == PlayerState.PLAYING) pause()
        else play()
    }

    fun getPos(): Int {
        return mediaPlayerInteractor.getPos()
    }

    fun getState(): PlayerState {
        return mediaPlayerInteractor.getState()
    }

    fun onActivityPaused() {
        pause()
    }

    fun onActivityDestroyed() {
        mediaPlayerInteractor.stop()
    }
}