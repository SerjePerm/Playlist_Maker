package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.domain.FavoritesInteractor
import com.example.playlistmaker.mediateka.domain.PlaylistsInteractor
import com.example.playlistmaker.mediateka.domain.models.Playlist
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    //Screen state
    private val _screenState = MutableLiveData<PlayerScreenState>(PlayerScreenState.Loading)
    val screenState: LiveData<PlayerScreenState> = _screenState
    private val _isFavorite = MutableLiveData(false)
    val isFavorite: LiveData<Boolean> = _isFavorite
    private var trackId = -1
    private val _playlists = MutableLiveData<List<Playlist>>(emptyList())
    val playlists: LiveData<List<Playlist>> = _playlists

    //For timer
    private var timerJob: Job? = null

    init {
        _screenState.value = PlayerScreenState.Content()
        viewModelScope.launch {
            favoritesInteractor.favoriteIds().collect { ids ->
                _isFavorite.value = ids.contains(trackId)
            }
        }
        viewModelScope.launch {
            playlistsInteractor.playlists().collect { list ->
                _playlists.value = list
            }
        }
    }

    private fun playerPlay() {
        mediaPlayerInteractor.play()
        startTimer()
    }

    private fun playerPause() {
        timerJob?.cancel()
        mediaPlayerInteractor.pause()
        updatePlayerInfo()
    }

    private fun updatePlayerInfo() {
        val tmpPlayerState = mediaPlayerInteractor.getState()
        val tmpPlayerPos = mediaPlayerInteractor.getPos()
        _screenState.value = PlayerScreenState.Content(tmpPlayerState, tmpPlayerPos)
    }

    fun setDataSource(track: Track) {
        mediaPlayerInteractor.setDataSource(track.previewUrl)
        trackId = track.trackId
    }

    fun playPauseClick() {
        if (mediaPlayerInteractor.getState() == PlayerState.PLAYING) playerPause()
        else playerPlay()
    }

    fun onActivityPaused() {
        playerPause()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.stop()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayerInteractor.getState() == PlayerState.PLAYING) {
                delay(TIMER_DELAY_MILLIS)
                updatePlayerInfo()
            }
        }
    }

    fun changeFavoriteClick(track: Track) {
        viewModelScope.launch {
            favoritesInteractor.changeFavorite(track)
        }
    }

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        /*
        val data: ArrayList<Int> = arrayListOf()
        val json = Gson().toJson(data)
        println("json: <$json>")

         */
        println("add TrackId $trackId to playlist ${playlist.title}")
        val playlistTracks: ArrayList<Int> = Gson().fromJson(
            playlist.tracks, object : TypeToken<ArrayList<Int>>() {}.type
        )
        playlistTracks.forEach { println("- trackId in playlist: $it") }
        if (playlistTracks.contains(trackId)) {
            println("track already in playlist!")
        } else {
            println("adding track to playlist...")
            viewModelScope.launch {
                playlistsInteractor.addTrackToPlaylist(track = track, playlist = playlist)
            }
        }
    }

    companion object {
        private const val TIMER_DELAY_MILLIS = 300L
    }

}