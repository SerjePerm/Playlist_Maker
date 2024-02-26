package com.example.playlistmaker.settings.domain

interface SettingsInteractor {
    fun loadIsDarkTheme(): Boolean
    fun saveIsDarkTheme(value: Boolean)
}