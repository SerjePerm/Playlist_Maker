package com.example.playlistmaker.di

import com.example.playlistmaker.mediateka.ui.addplaylist.AddPlaylistViewModel
import com.example.playlistmaker.mediateka.ui.favorites.FavoritesViewModel
import com.example.playlistmaker.mediateka.ui.mediateka.MediatekaViewModel
import com.example.playlistmaker.mediateka.ui.playlist.PlaylistViewModel
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
        PlayerViewModel(
            mediaPlayerInteractor = get(),
            favoritesInteractor = get(),
            playlistsInteractor = get()
        )
    }

    viewModel {
        SearchViewModel(tracksInteractor = get())
    }

    viewModel {
        SettingsViewModel(settingsInteractor = get(), sharingInteractor = get())
    }

    viewModel {
        FavoritesViewModel(favoritesInteractor = get())
    }

    viewModel {
        PlaylistsViewModel(playlistsInteractor = get())
    }

    viewModel {
        AddPlaylistViewModel(playlistsInteractor = get())
    }

    viewModel {
        PlaylistViewModel(playlistsInteractor = get(), sharingInteractor = get())
    }

}