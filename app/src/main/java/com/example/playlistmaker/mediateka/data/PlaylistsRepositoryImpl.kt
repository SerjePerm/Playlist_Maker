package com.example.playlistmaker.mediateka.data

import com.example.playlistmaker.mediateka.data.db.MainDB
import com.example.playlistmaker.mediateka.data.db.toPlaylist
import com.example.playlistmaker.mediateka.data.db.toPlaylistEntity
import com.example.playlistmaker.mediateka.data.db.toSelectedTrackEntity
import com.example.playlistmaker.mediateka.domain.ImagesRepository
import com.example.playlistmaker.mediateka.domain.PlaylistsRepository
import com.example.playlistmaker.mediateka.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.toIntList
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val db: MainDB,
    private val imagesRepository: ImagesRepository
) : PlaylistsRepository {

    override fun playlists(): Flow<List<Playlist>> {
        return db.playlistsDao().getAll().map { list ->
            list.map { it.toPlaylist(imagesRepository.filenameToUri(it.poster)) }
        }
    }

    override suspend fun getById(playlistId: Int): Playlist? {
        val playlist = db.playlistsDao().getById(playlistId)
        return playlist?.toPlaylist(imagesRepository.filenameToUri(playlist.poster))
    }

    override suspend fun upsertPlaylist(playlist: Playlist) {
        val filename = if (playlist.poster != null) { imagesRepository.save(playlist.poster) } else ""
        db.playlistsDao().upsert(playlist.toPlaylistEntity(filename))
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        db.playlistsDao().delete(playlistId)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        val tracks = playlist.tracks.toIntList()
        tracks.add(track.trackId)
        val newTracks = Gson().toJson(tracks)
        val newPlaylist = playlist.copy(tracks = newTracks, count = tracks.count())
        val filename = playlist.poster.toString().substringAfterLast('/')
        //
        println("from uri: ${playlist.poster}")
        println("to fileN: $filename")
        //
        val newPlEntity = newPlaylist.toPlaylistEntity(filename)
        db.playlistsDao().upsert(newPlEntity)
        db.selectedTracksDao().insert(track.toSelectedTrackEntity())
    }

}