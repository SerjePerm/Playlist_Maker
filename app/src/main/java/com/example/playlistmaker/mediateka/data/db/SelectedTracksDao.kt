package com.example.playlistmaker.mediateka.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectedTracksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(track: SelectedTrackEntity)

    @Query("SELECT * FROM selected_tracks_table WHERE trackId = :trackId")
    suspend fun getById(trackId: Int): SelectedTrackEntity?

    @Delete
    suspend fun delete(track: SelectedTrackEntity)

}