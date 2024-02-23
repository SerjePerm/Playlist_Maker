package com.example.playlistmaker.settings.data

interface SettingsRepository {
    fun loadIsDarkTheme(): Boolean
    fun saveIsDarkTheme(value: Boolean)
}