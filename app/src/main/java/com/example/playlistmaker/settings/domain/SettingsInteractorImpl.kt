package com.example.playlistmaker.settings.domain

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {

    override fun loadIsDarkTheme(): Boolean {
        return settingsRepository.loadIsDarkTheme()
    }

    override fun saveIsDarkTheme(value: Boolean) {
        settingsRepository.saveIsDarkTheme(value)
    }

}