package com.example.playlistmaker.data

import com.example.playlistmaker.utils.msToTime
import java.io.Serializable

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) : Serializable {
    val bigCoverUrl: String
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    val releaseYear: String
        get() = releaseDate.substringBefore('-')
    val trackTime: String
        get() = msToTime(trackTimeMillis)
}