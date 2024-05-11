package com.example.playlistmaker.mediateka.data

import com.example.playlistmaker.mediateka.data.db.MainDB
import com.example.playlistmaker.mediateka.data.db.toTrack
import com.example.playlistmaker.mediateka.data.db.toTrackEntity
import com.example.playlistmaker.mediateka.domain.FavoritesRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(private val db: MainDB) : FavoritesRepository {

    override fun favoriteTracks(): Flow<List<Track>> {
        return db.favoritesDao().getAll().map { list ->
            list.map { it.toTrack() }
        }
    }

    override fun favoriteIds(): Flow<List<Int>> {
        return db.favoritesDao().getIds()
    }

    override suspend fun getById(trackId: Int): Track? {
        return db.favoritesDao().getById(trackId)?.toTrack()
    }

    override suspend fun upsertTrack(track: Track) {
        db.favoritesDao().upsert(track.toTrackEntity())
    }

    override suspend fun deleteTrack(trackId: Int) {
        db.favoritesDao().delete(trackId)
    }


}