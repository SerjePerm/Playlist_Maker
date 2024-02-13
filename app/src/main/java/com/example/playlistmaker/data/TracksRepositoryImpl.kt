package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TracksResponse

class TracksRepositoryImpl(private val networkClient: NetworkClient): TracksRepository {

    override fun searchTracks(expression: String): TracksResponse {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            val trackList = (response as TracksSearchResponse).results.map {
                Track(
                    trackId = it.trackId,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTimeMillis = it.trackTimeMillis,
                    artworkUrl100 = it.artworkUrl100,
                    collectionName = it.collectionName,
                    releaseDate = it.releaseDate,
                    primaryGenreName = it.primaryGenreName,
                    country = it.country,
                    previewUrl = it.previewUrl
                )
            }
            return TracksResponse(trackList, response.resultCode)
        } else return TracksResponse(emptyList(), response.resultCode)
    }
}