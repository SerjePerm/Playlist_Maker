package com.example.playlistmaker.sharing.domain

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

    override fun shareText(text: String) {
        sharingData.shareText(text)
    }

}