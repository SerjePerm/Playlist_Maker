package com.example.playlistmaker.mediateka.data.db

import android.net.Uri
import com.example.playlistmaker.mediateka.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track

fun Track.toTrackEntity(): TrackEntity {
    return TrackEntity(
        id = null,
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = this.trackTimeMillis,
        artworkUrl100 = this.artworkUrl100,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        previewUrl = this.previewUrl
    )
}

fun TrackEntity.toTrack(): Track {
    return Track(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = this.trackTimeMillis,
        artworkUrl100 = this.artworkUrl100,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        previewUrl = this.previewUrl
    )
}

fun Track.toSelectedTrackEntity(): SelectedTrackEntity {
    return SelectedTrackEntity(
        id = null,
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = this.trackTimeMillis,
        artworkUrl100 = this.artworkUrl100,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        previewUrl = this.previewUrl
    )
}

fun SelectedTrackEntity.toTrack(): Track {
    return Track(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = this.trackTimeMillis,
        artworkUrl100 = this.artworkUrl100,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        previewUrl = this.previewUrl
    )
}

fun Playlist.toPlaylistEntity(filename: String): PlaylistEntity {
    return PlaylistEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        poster = filename,
        tracks = this.tracks,
        count = this.count
    )
}

fun PlaylistEntity.toPlaylist(uri: Uri?): Playlist {
    return Playlist(
        id = this.id,
        title = this.title,
        description = this.description,
        poster = uri,
        tracks = this.tracks,
        count = this.count
    )
}