package com.example.playlistmaker.mediateka.data

import com.example.playlistmaker.mediateka.data.db.MainDB
import com.example.playlistmaker.mediateka.data.db.toPlaylist
import com.example.playlistmaker.mediateka.data.db.toPlaylistEntity
import com.example.playlistmaker.mediateka.domain.PlaylistsRepository
import com.example.playlistmaker.mediateka.domain.models.Playlist
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

}