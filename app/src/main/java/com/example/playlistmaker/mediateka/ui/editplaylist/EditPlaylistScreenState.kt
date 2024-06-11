package com.example.playlistmaker.mediateka.ui.editplaylist

import android.net.Uri

data class EditPlaylistScreenState(
    val id: Int? = null,
    val title: String = "",
    val description: String = "",
    val poster: Uri? = null,
    val tracks: String = "",
    val count: Int = 0,
    val isTitleNotEmpty: Boolean = false
)