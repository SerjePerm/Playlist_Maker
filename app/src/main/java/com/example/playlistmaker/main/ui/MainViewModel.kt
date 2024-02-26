package com.example.playlistmaker.main.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class MainViewModel : ViewModel() {

    private val _navigatePath = MutableLiveData(MainPaths.NONE)
    val navigatePath: LiveData<MainPaths> = _navigatePath

    fun searchClick() {
        _navigatePath.value = MainPaths.SEARCH
    }

    fun mediatekaClick() {
        _navigatePath.value = MainPaths.MEDIATEKA
    }

    fun settingsClick() {
        _navigatePath.value = MainPaths.SETTINGS
    }

    companion object{
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer { MainViewModel() }
        }
    }

}