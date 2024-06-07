package com.example.playlistmaker.mediateka.ui.editplaylist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.domain.PlaylistsInteractor
import com.example.playlistmaker.mediateka.domain.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    //Screen state
    private val _screenState = MutableLiveData<EditPlaylistScreenState>()
    val screenState: LiveData<EditPlaylistScreenState> = _screenState

    init {
        _screenState.value = EditPlaylistScreenState()
    }

    fun setPlaylistToState(playlist: Playlist) {
        with(playlist) {
            _screenState.value = EditPlaylistScreenState(
                id = id,
                title = title,
                description = description ?: "",
                poster = poster,
                tracks = tracks,
                count = count,
                isTitleNotEmpty = true
            )
        }
    }

    fun saveClick() {
        viewModelScope.launch(Dispatchers.IO) {
            val newPlaylist = Playlist(
                id = _screenState.value!!.id,
                title = _screenState.value!!.title,
                description = _screenState.value!!.description,
                poster = _screenState.value!!.poster,
                tracks = _screenState.value!!.tracks,
                count = _screenState.value!!.count
            )
            playlistsInteractor.upsertPlaylist(newPlaylist)
        }
    }

    fun changeTitle(title: String) {
        _screenState.value = _screenState.value?.copy(title = title)
        _screenState.value = _screenState.value?.copy(isTitleNotEmpty = title.isNotBlank())
    }

    fun changeDescription(description: String) {
        _screenState.value = _screenState.value?.copy(description = description)
    }

    fun changeUri(uri: Uri) {
        _screenState.value = _screenState.value?.copy(poster = uri)
    }

}