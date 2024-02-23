package com.example.playlistmaker.search.domain.models

import com.example.playlistmaker.search.domain.models.Track

data class TracksResponse(val results: List<Track>, val resultCode: Int)