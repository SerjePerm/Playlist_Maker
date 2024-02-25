package com.example.playlistmaker.settings.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.utils.SingleLiveEvent

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val _isDarkTheme = MutableLiveData(false)
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    init {
        _isDarkTheme.value = settingsInteractor.loadIsDarkTheme()
    }

    fun changeTheme(value: Boolean) {
        if (_isDarkTheme.value != value) {
            _isDarkTheme.value = value
            settingsInteractor.saveIsDarkTheme(value)
            if (value) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun supportContact() {
        sharingInteractor.supportContact()
    }

    fun userLicense() {
        sharingInteractor.userLicense()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                SettingsViewModel(
                    settingsInteractor = Creator.provideSettingsInteractor(application),
                    sharingInteractor = Creator.provideSharingInteractor(application)
                )
            }
        }
    }

}