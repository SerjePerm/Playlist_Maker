package com.example.playlistmaker.mediateka.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistsDao {

    @Query("SELECT * FROM playlists_table ORDER BY title DESC")
    fun getAll(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists_table WHERE id = :playlistId")
    fun getById(playlistId: Int): Flow<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(playlistEntity: PlaylistEntity)

    @Query("DELETE FROM playlists_table WHERE id = :playlistId")
    suspend fun delete(playlistId: Int)

    @Query("SELECT * FROM playlists_table")
    fun getAllOnce(): List<PlaylistEntity>
}