package com.example.playlistmaker.sharing.domain

interface SharingData {
    fun shareApp()
    fun supportContact()
    fun userLicense()
    fun shareText(text: String)
}