package com.example.playlistmaker.mediateka.domain

import android.net.Uri

interface ImagesRepository {
    suspend fun save(uri: Uri): String
    suspend fun filenameToUri(filename: String): Uri?
}