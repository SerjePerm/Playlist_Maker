package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.data.MediaPlayerDataImpl
import com.example.playlistmaker.data.TracksHistoryRepositoryImpl
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.presentation.presenters.mediaplayer.MediaPlayerPresenter
import com.example.playlistmaker.presentation.presenters.mediaplayer.MediaPlayerView

object Creator {

    fun provideTracksInteractor(sharedPreferences: SharedPreferences): TracksInteractor {
        return TracksInteractorImpl(
            repository = TracksRepositoryImpl(RetrofitNetworkClient()),
            history = TracksHistoryRepositoryImpl(sharedPreferences)
        )
    }

    fun provideMediaPlayerPresenter(view: MediaPlayerView, url: String): MediaPlayerPresenter {
        return MediaPlayerPresenter(
            view = view,
            mediaPlayerInteractor = MediaPlayerInteractorImpl(MediaPlayerDataImpl(url))
        )
    }

}