package com.example.playlistmaker.mediateka.data

import com.example.playlistmaker.mediateka.data.db.MainDB
import com.example.playlistmaker.mediateka.data.db.toPlaylist
import com.example.playlistmaker.mediateka.data.db.toPlaylistEntity
import com.example.playlistmaker.mediateka.data.db.toSelectedTrackEntity
import com.example.playlistmaker.mediateka.domain.PlaylistsRepository
import com.example.playlistmaker.mediateka.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(private val db: MainDB) : PlaylistsRepository {

    override fun playlists(): Flow<List<Playlist>> {
        return db.playlistsDao().getAll().map { list ->
            list.map { it.toPlaylist() }
        }
    }

    override suspend fun getById(playlistId: Int): Playlist? {
        return db.playlistsDao().getById(playlistId)?.toPlaylist()
    }

    override suspend fun upsertPlaylist(playlist: Playlist) {
        db.playlistsDao().upsert(playlist.toPlaylistEntity())
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        db.playlistsDao().delete(playlistId)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        val tracks: ArrayList<Int> = Gson().fromJson(playlist.tracks, object : TypeToken<ArrayList<Int>>() {}.type)
        tracks.add(track.trackId)
        val newTracks = Gson().toJson(tracks)
        val newPlaylist = playlist.copy(tracks = newTracks)
        db.playlistsDao().upsert(newPlaylist.toPlaylistEntity())
        db.selectedTracksDao().insert(track.toSelectedTrackEntity())
    }

}