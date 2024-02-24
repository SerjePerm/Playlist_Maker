package com.example.playlistmaker.player.ui

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState

class PlayerViewModel(private val mediaPlayerInteractor: MediaPlayerInteractor) : ViewModel() {

    //For timer
    private val handler = android.os.Handler(Looper.getMainLooper())
    private val timerRunnable = Runnable { timerTask() }
    //Player state
    private val _playerState = MutableLiveData(PlayerState.DEFAULT)
    val playerState: LiveData<PlayerState> = _playerState
    //Player pos
    private val _playerPos = MutableLiveData(0)
    val playerPos: LiveData<Int> = _playerPos

    private fun playerPlay() {
        mediaPlayerInteractor.play()
        handler.post(timerRunnable)
    }

    private fun playerPause() {
        mediaPlayerInteractor.pause()
        updatePlayerStateAndPos()
    }

    private fun updatePlayerStateAndPos() {
        _playerState.value = mediaPlayerInteractor.getState()
        _playerPos.value = mediaPlayerInteractor.getPos()
    }

    fun playPauseClick() {
        if (mediaPlayerInteractor.getState() == PlayerState.PLAYING) playerPause()
        else playerPlay()
    }

    fun onActivityPaused() {
        playerPause()
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(TIMER_TOKEN)
        mediaPlayerInteractor.stop()
    }

    private fun timerTask() {
        updatePlayerStateAndPos()
        if (mediaPlayerInteractor.getState() == PlayerState.PLAYING) {
            handler.postDelayed(timerRunnable, TIMER_TOKEN, TIMER_DELAY)
        }
    }

    companion object{
        private val TIMER_TOKEN = Any()
        private const val TIMER_DELAY = 500L

        fun getViewModelFactory(previewUrl: String): ViewModelProvider.Factory = viewModelFactory {
            initializer { PlayerViewModel(
                 mediaPlayerInteractor = Creator.provideMediaPlayerInteractor(previewUrl)
            ) }
        }
    }

}