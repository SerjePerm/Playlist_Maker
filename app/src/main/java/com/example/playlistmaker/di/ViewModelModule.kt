package com.example.playlistmaker.di

import com.example.playlistmaker.mediateka.ui.addplaylist.AddPlaylistViewModel
import com.example.playlistmaker.mediateka.ui.favorites.FavoritesViewModel
import com.example.playlistmaker.mediateka.ui.mediateka.MediatekaViewModel
import com.example.playlistmaker.mediateka.ui.playlists.PlaylistsViewModel
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MediatekaViewModel()
    }

    viewModel {
        PlayerViewModel(get(), get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        PlaylistsViewModel()
    }

    viewModel {
        AddPlaylistViewModel()
    }

}