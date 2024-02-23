package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.sharing.data.SharingData

class SharingInteractorImpl(private val sharingData: SharingData) : SharingInteractor {

    override fun shareApp() {
        sharingData.shareApp()
    }

    override fun supportContact() {
        sharingData.supportContact()
    }

    override fun userLicense() {
        sharingData.userLicense()
    }
    
}