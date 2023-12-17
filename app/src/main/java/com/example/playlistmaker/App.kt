package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val PREFERENCES_TITLE = "PlayList_Maker_preferences"
const val DARK_THEME =  "dark_theme"
const val SEARCH_HISTORY = "search_history"

class App : Application() {

    var isDarkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        //load isDarkTheme from SharedPreferences
        sharedPrefs = getSharedPreferences(PREFERENCES_TITLE, MODE_PRIVATE)
        isDarkTheme = sharedPrefs.getBoolean(DARK_THEME, false)
        switchTheme(darkTheme = isDarkTheme)
    }

    fun switchTheme(darkTheme: Boolean) {
        isDarkTheme = darkTheme
        sharedPrefs.edit().putBoolean(DARK_THEME, isDarkTheme).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}