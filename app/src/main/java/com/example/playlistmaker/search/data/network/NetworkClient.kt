package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest

interface NetworkClient {
    suspend fun doRequest(request: TracksSearchRequest): Response
}