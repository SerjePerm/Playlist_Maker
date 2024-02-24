package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setLightOrDarkTheme()
    }

    private fun setLightOrDarkTheme() {
        val settingsInteractor = Creator.provideSettingsInteractor(this)
        val isDarkTheme = settingsInteractor.loadIsDarkTheme()
        if (isDarkTheme) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}