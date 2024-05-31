package com.example.playlistmaker.mediateka.ui.addplaylist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.domain.PlaylistsInteractor
import com.example.playlistmaker.mediateka.domain.models.Playlist
import kotlinx.coroutines.launch

class AddPlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    //Screen state
    private val _screenState = MutableLiveData<AddPlaylistScreenState>()
    val screenState: LiveData<AddPlaylistScreenState> = _screenState

    init {
        _screenState.value = AddPlaylistScreenState()
    }

    fun createClick() {
        viewModelScope.launch {
            val newPlaylist = Playlist(
                id = null,
                title = _screenState.value!!.title,
                description = _screenState.value!!.description,
                poster = _screenState.value!!.uri,
                tracks = "[]",
                count = 0
            )
            playlistsInteractor.upsertPlaylist(newPlaylist)
        }
    }

    fun changeTitle(title: String) {
        _screenState.value = _screenState.value?.copy(title = title)
        _screenState.value = _screenState.value?.copy(isTitleNotEmpty = title.isNotBlank())
        checkChanges()
    }

    fun changeDescription(description: String) {
        _screenState.value = _screenState.value?.copy(description = description)
        checkChanges()
    }

    fun changeUri(uri: Uri) {
        _screenState.value = _screenState.value?.copy(uri = uri)
        checkChanges()
    }

    private fun checkChanges() {
        val isChangesExist = _screenState.value?.title?.isNotBlank() == true ||
                _screenState.value?.description?.isNotBlank() == true ||
                _screenState.value?.uri != null
        _screenState.value = _screenState.value?.copy(isChangesExist = isChangesExist)
    }

}