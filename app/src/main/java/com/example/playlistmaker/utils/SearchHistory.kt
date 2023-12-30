package com.example.playlistmaker.utils

import android.content.SharedPreferences
import com.example.playlistmaker.SEARCH_HISTORY
import com.example.playlistmaker.data.Track
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun saveToHistory(track: Track) {
        val array = loadHistory()
        if (array.contains(track)) array.remove(track)
        if (array.size>9) array.removeAt(0)
        array.add(track)
        val json = Gson().toJson(array)
        sharedPreferences.edit().putString(SEARCH_HISTORY, json).apply()
    }

    fun loadHistory() : ArrayList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY, null) ?: return arrayListOf()
        return ArrayList(Gson().fromJson(json, Array<Track>::class.java).toList())
     }

    fun clearHistory() {
        sharedPreferences.edit().remove(SEARCH_HISTORY).apply()
    }

}