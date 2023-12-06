package com.example.playlistmaker.iTunes

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search")
    fun findTrack(@Query("term") text: String): Call<TracksResponse>
}