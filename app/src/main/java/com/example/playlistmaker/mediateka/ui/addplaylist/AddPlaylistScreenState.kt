package com.example.playlistmaker.mediateka.ui.addplaylist

import android.net.Uri

data class AddPlaylistScreenState(
    val title: String = "",
    val description: String = "",
    val uri: Uri? = null,
    val isChangesExist: Boolean = false,
    val isTitleNotEmpty: Boolean = false
)