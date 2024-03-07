package com.example.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor

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

}