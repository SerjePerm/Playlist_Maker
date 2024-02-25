package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.MediaPlayerData
import com.example.playlistmaker.player.domain.PlayerState

class MediaPlayerDataImpl(url: String) : MediaPlayerData {

    private var mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.DEFAULT

    init {
        mediaPlayer.setDataSource(url)
        mediaPlayer.setOnCompletionListener { playerState = PlayerState.PREPARED }
    }

    override fun getPlayerState(): PlayerState {
        return playerState
    }

    override fun getPos(): Int {
        return mediaPlayer.currentPosition
    }

    override fun play() {
        if (playerState == PlayerState.DEFAULT) {
            mediaPlayer.prepare()
            playerState = PlayerState.PREPARED
        }
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSED
    }

    override fun destroy() {
        mediaPlayer.release()
    }
}