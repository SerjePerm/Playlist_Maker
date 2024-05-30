package com.example.playlistmaker.mediateka.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val title: String,
    val description: String?,
    val poster: String,
    val tracks: String,
    val count: Int
)