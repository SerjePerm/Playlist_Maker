package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.player.data.MediaPlayerDataImpl
import com.example.playlistmaker.player.domain.MediaPlayerData
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.sharing.data.SharingDataImpl
import com.example.playlistmaker.sharing.domain.SharingData
import com.example.playlistmaker.utils.Constants
import com.example.playlistmaker.utils.Constants.Companion.I_TUNES_BASE_URL
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl(I_TUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single {
        androidContext().getSharedPreferences(Constants.PREFERENCES_TITLE, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    factory { MediaPlayer() }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    factory<MediaPlayerData> {
        MediaPlayerDataImpl(get())
    }

    single<SharingData> {
        SharingDataImpl(androidContext())
    }

}