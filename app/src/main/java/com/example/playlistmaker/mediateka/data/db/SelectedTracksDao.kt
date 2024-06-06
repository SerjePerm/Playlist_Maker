package com.example.playlistmaker.mediateka.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SelectedTracksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(track: SelectedTrackEntity)

    @Query("SELECT * FROM selected_tracks_table WHERE trackId = :trackId")
    suspend fun getById(trackId: Int): SelectedTrackEntity

    @Query("DELETE FROM selected_tracks_table WHERE trackId = :trackId")
    fun delete(trackId: Int)

}