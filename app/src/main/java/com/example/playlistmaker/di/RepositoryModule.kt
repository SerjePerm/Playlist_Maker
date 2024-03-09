package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.TracksHistoryRepositoryImpl
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.TracksHistoryRepository
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<TracksHistoryRepository> {
        TracksHistoryRepositoryImpl(get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

}