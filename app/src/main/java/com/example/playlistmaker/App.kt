package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val SEARCH_HISTORY = "search_history"

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

    companion object {
        const val PREFERENCES_TITLE = "PlayList_Maker_preferences"
        const val DARK_THEME = "dark_theme"
    }

}