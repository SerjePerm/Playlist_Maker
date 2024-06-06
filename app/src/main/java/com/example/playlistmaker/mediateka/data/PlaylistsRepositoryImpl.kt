package com.example.playlistmaker.mediateka.data

import com.example.playlistmaker.mediateka.data.db.MainDB
import com.example.playlistmaker.mediateka.data.db.toPlaylist
import com.example.playlistmaker.mediateka.data.db.toPlaylistEntity
import com.example.playlistmaker.mediateka.data.db.toSelectedTrackEntity
import com.example.playlistmaker.mediateka.data.db.toTrack
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

    override fun getById(playlistId: Int): Flow<Playlist> {
        return db.playlistsDao().getById(playlistId).map { playlistEntity ->
            playlistEntity.toPlaylist(imagesRepository.filenameToUri(playlistEntity.poster))
        }
    }

    override suspend fun upsertPlaylist(playlist: Playlist) {
        val filename = if (playlist.poster != null) { imagesRepository.save(playlist.poster) } else ""
        db.playlistsDao().upsert(playlist.toPlaylistEntity(filename))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        val tracks = playlist.tracks.toIntList()
        db.playlistsDao().delete(playlist.id!!)
        tracks.forEach { checkTrackToDelete(it) }
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        val tracks = playlist.tracks.toIntList()
        tracks.add(track.trackId)
        val newTracks = Gson().toJson(tracks)
        val newPlaylist = playlist.copy(tracks = newTracks, count = tracks.count())
        var filename = ""
        if (playlist.poster!=null) filename = playlist.poster.toString().substringAfterLast('/')
        val newPlEntity = newPlaylist.toPlaylistEntity(filename)
        db.playlistsDao().upsert(newPlEntity)
        db.selectedTracksDao().insert(track.toSelectedTrackEntity())
    }

    override suspend fun delTrackFromPlaylist(track: Track, playlist: Playlist) {
        val tracks = playlist.tracks.toIntList()
        tracks.remove(track.trackId)
        val newTracks = Gson().toJson(tracks)
        val newPlaylist = playlist.copy(tracks = newTracks, count = tracks.count())
        var filename = ""
        if (playlist.poster!=null) filename = playlist.poster.toString().substringAfterLast('/')
        val newPlEntity = newPlaylist.toPlaylistEntity(filename)
        db.playlistsDao().upsert(newPlEntity)
        checkTrackToDelete(track.trackId)
    }

    override suspend fun getTrackById(trackId: Int): Track {
        return db.selectedTracksDao().getById(trackId).toTrack()
    }

    private fun checkTrackToDelete(trackId: Int) {
        val playlists = db.playlistsDao().getAllOnce()
        var count = 0
        playlists.forEach { playlist ->
            val tracks = playlist.tracks.toIntList()
            if (tracks.contains(trackId)) { count += 1 }
        }
        if (count == 0) { db.selectedTracksDao().delete(trackId) }
    }

}