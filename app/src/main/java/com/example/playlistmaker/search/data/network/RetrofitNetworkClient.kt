package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val iTunesService: ITunesApi) : NetworkClient {

    override suspend fun doRequest(request: TracksSearchRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                val response = iTunesService.findTrack(request.expression)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 400 }
            }
        }
    }

}