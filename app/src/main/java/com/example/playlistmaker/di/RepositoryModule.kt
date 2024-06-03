package com.example.playlistmaker.di

import com.example.playlistmaker.mediateka.data.ImagesRepositoryImpl
import com.example.playlistmaker.mediateka.data.PlaylistsRepositoryImpl
import com.example.playlistmaker.mediateka.domain.ImagesRepository
import com.example.playlistmaker.mediateka.domain.PlaylistsRepository
import com.example.playlistmaker.search.data.TracksHistoryRepositoryImpl
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.TracksHistoryRepository
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<SettingsRepository> {
        SettingsRepositoryImpl(sharedPreferences = get())
    }

    single<TracksHistoryRepository> {
        TracksHistoryRepositoryImpl(sharedPreferences = get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(networkClient = get())
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(db = get(), imagesRepository = get())
    }

    single<ImagesRepository> {
        ImagesRepositoryImpl(context = get())
    }

}