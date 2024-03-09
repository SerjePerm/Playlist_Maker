package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest

class RetrofitNetworkClient(private val iTunesService: ITunesApi) : NetworkClient {

    override fun doRequest(request: TracksSearchRequest): Response {
        return try {
            val resp = iTunesService.findTrack(request.expression).execute()
            val body = resp.body() ?: Response()
            body.apply { resultCode = resp.code() }
        } catch (exception: Exception) {
            Response().apply { resultCode = 400 }
        }
    }

}