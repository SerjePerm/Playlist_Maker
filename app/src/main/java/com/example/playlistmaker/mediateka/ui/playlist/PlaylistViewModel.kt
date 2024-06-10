package com.example.playlistmaker.mediateka.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.domain.PlaylistsInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.utils.toIntList
import com.example.playlistmaker.utils.toTracksCount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<PlaylistScreenState>()
    val screenState: LiveData<PlaylistScreenState> = _screenState
    private var job: Job? = null

    init {
        _screenState.value = PlaylistScreenState()
    }

    fun setPlaylistId(playlistId: Int) {
        job = viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getById(playlistId).collect{ playlist ->
                val tracksIds = playlist.tracks.toIntList()
                val tracksList = ArrayList<Track>()
                var tracksLength = 0
                tracksIds.forEach { trackId ->
                    val track = playlistsInteractor.getTrackById(trackId)
                    tracksList.add(track)
                    tracksLength += track.trackTimeMillis
                }
                _screenState.postValue(
                    PlaylistScreenState(
                        playlist = playlist,
                        trackList = tracksList,
                        tracksLength = tracksLength / 60000
                    )
                )
            }
        }
    }

    fun sharePlaylist() {
        val pl = _screenState.value!!.playlist
        val text = StringBuilder("")
        text.append(pl.title + "\n")
        text.append(pl.description + "\n")
        text.append(pl.count.toTracksCount() + "\n")
        _screenState.value!!.trackList.forEachIndexed { index, track ->
            text.append("${index+1}.${track.artistName} - ${track.trackName} (${track.trackTime})\n")
        }
        sharingInteractor.shareText(text.toString())
    }

    fun delTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.delTrackFromPlaylist(track, _screenState.value!!.playlist)
        }
    }

    fun delPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            job?.cancel()
            _screenState.value?.playlist?.let { playlistsInteractor.delPlaylist(it) }
        }
    }

}