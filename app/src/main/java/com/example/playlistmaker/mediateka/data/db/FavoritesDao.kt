package com.example.playlistmaker.mediateka.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favorite_tracks_table ORDER BY id DESC")
    fun getAll(): Flow<List<TrackEntity>>

    @Query("SELECT trackId FROM favorite_tracks_table")
    fun getIds(): Flow<List<Int>>

    @Query("SELECT * FROM favorite_tracks_table WHERE trackId = :trackId")
    suspend fun getById(trackId: Int): TrackEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(trackEntity: TrackEntity)

    @Query("DELETE FROM favorite_tracks_table WHERE trackId = :trackId")
    suspend fun delete(trackId: Int)

}