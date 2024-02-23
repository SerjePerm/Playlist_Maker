package com.example.playlistmaker.player.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.models.Track

class PlayerViewModel(
    private val track: Track,
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {

    private fun play() {
        mediaPlayerInteractor.play()
        //view.setState(PlayerState.PLAYING)
    }

    private fun pause() {
        mediaPlayerInteractor.pause()
        //view.setState(PlayerState.PAUSED)
    }

    fun playPauseClick() {
        if (mediaPlayerInteractor.getState() == PlayerState.PLAYING) pause()
        else play()
    }

    fun onActivityPaused() {
        pause()
    }

    fun onActivityDestroyed() {
        mediaPlayerInteractor.stop()
    }

    companion object{
        fun getViewModelFactory(track: Track): ViewModelProvider.Factory = viewModelFactory {
            initializer { PlayerViewModel(
                track = track,
                mediaPlayerInteractor = Creator.provideMediaPlayerInteractor(track.previewUrl)
            ) }
        }
    }

}