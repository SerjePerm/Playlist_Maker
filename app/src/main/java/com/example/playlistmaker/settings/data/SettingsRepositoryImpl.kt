package com.example.playlistmaker.settings.data

import android.content.SharedPreferences
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.utils.Constants.Companion.DARK_THEME

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {

    override fun loadIsDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME, false)
    }

    override fun saveIsDarkTheme(value: Boolean) {
        sharedPreferences.edit().putBoolean(DARK_THEME, value).apply()
    }

}