package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(private val mediaPlayerInteractor: MediaPlayerInteractor) : ViewModel() {

    //Screen state
    private val _screenState = MutableLiveData<PlayerScreenState>(PlayerScreenState.Loading)
    val screenState: LiveData<PlayerScreenState> = _screenState

    //For timer
    private var timerJob: Job? = null

    init {
        _screenState.value = PlayerScreenState.Content()
    }

    private fun playerPlay() {
        mediaPlayerInteractor.play()
        startTimer()
    }

    private fun playerPause() {
        timerJob?.cancel()
        mediaPlayerInteractor.pause()
        updatePlayerInfo()
    }

    private fun updatePlayerInfo() {
        val tmpPlayerState = mediaPlayerInteractor.getState()
        val tmpPlayerPos = mediaPlayerInteractor.getPos()
        _screenState.value = PlayerScreenState.Content(tmpPlayerState, tmpPlayerPos)
    }

    fun setDataSource(url: String) {
        mediaPlayerInteractor.setDataSource(url)
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
        mediaPlayerInteractor.stop()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayerInteractor.getState() == PlayerState.PLAYING) {
                delay(TIMER_DELAY_MILLIS)
                updatePlayerInfo()
            }
        }
    }

    companion object {
        private const val TIMER_DELAY_MILLIS = 300L
    }

}