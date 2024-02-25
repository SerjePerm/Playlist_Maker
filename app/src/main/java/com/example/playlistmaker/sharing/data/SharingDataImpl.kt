package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.getString
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.SharingData

class SharingDataImpl(private val context: Context) : SharingData {

    override fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, getString(context, R.string.share_text))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun supportContact() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse(getString(context, R.string.support_adress))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(context, R.string.support_subject))
        intent.putExtra(Intent.EXTRA_TEXT, getString(context, R.string.support_text))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun userLicense() {
        val url = Uri.parse(getString(context, R.string.user_license_URL))
        val intent = Intent(Intent.ACTION_VIEW, url)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

}