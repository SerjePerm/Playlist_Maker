package com.example.playlistmaker.mediateka.ui.addplaylist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddPlaylistViewModel : ViewModel() {

    //Screen state
    private val _screenState = MutableLiveData<AddPlaylistScreenState>()
    val screenState: LiveData<AddPlaylistScreenState> = _screenState

    init {
        _screenState.value = AddPlaylistScreenState()
    }

    fun changeTitle(title: String) {
        val bool = title.isNotBlank()
        println("changing title to _${title}_")
        println("isNotBlank: ${bool}")
        _screenState.value = _screenState.value?.copy(title = title)
        _screenState.value = _screenState.value?.copy(isTitleNotEmpty = bool)
    }

    fun changeDescription(description: String) {
        _screenState.value = _screenState.value?.copy(description = description)
    }

    fun changeUri(uri: Uri) {
        _screenState.value = _screenState.value?.copy(uri = uri)
    }

}