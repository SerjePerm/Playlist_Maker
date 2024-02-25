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

    //Screen state
    private val _screenState = MutableLiveData<PlayerScreenState>(PlayerScreenState.Loading)
    val screenState: LiveData<PlayerScreenState> = _screenState
    //For timer
    private val handler = android.os.Handler(Looper.getMainLooper())
    private val timerRunnable = Runnable { timerTask() }

    init {
        _screenState.value = PlayerScreenState.Content()
    }

    private fun playerPlay() {
        mediaPlayerInteractor.play()
        handler.post(timerRunnable)
    }

    private fun playerPause() {
        mediaPlayerInteractor.pause()
        updatePlayerInfo()
    }

    private fun updatePlayerInfo() {
        val tmpPlayerState = mediaPlayerInteractor.getState()
        val tmpPlayerPos = mediaPlayerInteractor.getPos()
        _screenState.value = PlayerScreenState.Content(tmpPlayerState, tmpPlayerPos)
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
        updatePlayerInfo()
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