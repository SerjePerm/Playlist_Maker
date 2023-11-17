package com.example.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //view val
        val tvGoBack = findViewById<TextView>(R.id.tvGoBack)
        val frameShareApp = findViewById<FrameLayout>(R.id.frameShareApp)
        val frameSupportContact = findViewById<FrameLayout>(R.id.frameSupportContact)
        val frameUserLicense = findViewById<FrameLayout>(R.id.frameUserLicense)
        val frameDarkTheme = findViewById<FrameLayout>(R.id.frameDarkTheme)
        val switchDark = findViewById<Switch>(R.id.switchDark)
        switchDark.isChecked = isDarkTheme()
        // GoBack button
        tvGoBack.setOnClickListener { finish() }
        // DarkTheme button
        val darkThemeListener = View.OnClickListener {
            if (isDarkTheme()) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        frameDarkTheme.setOnClickListener(darkThemeListener)
        switchDark.setOnClickListener(darkThemeListener)
        // ShareApp button
        frameShareApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
            startActivity(intent)
        }
        // SupportContact button
        frameSupportContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse(getString(R.string.support_adress))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_text))
            startActivity(intent)
        }
        // UserLicense button
        frameUserLicense.setOnClickListener {
            val url = Uri.parse(getString(R.string.user_license_URL))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
    }

    private fun isDarkTheme(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}