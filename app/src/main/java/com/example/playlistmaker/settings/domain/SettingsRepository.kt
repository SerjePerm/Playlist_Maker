package com.example.playlistmaker.settings.domain

interface SettingsRepository {
    fun loadIsDarkTheme(): Boolean
    fun saveIsDarkTheme(value: Boolean)
}