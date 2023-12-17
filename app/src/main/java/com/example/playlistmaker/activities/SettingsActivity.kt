package com.example.playlistmaker.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //view val
        val tvGoBack = findViewById<TextView>(R.id.tvGoBack)
        val frameShareApp = findViewById<FrameLayout>(R.id.frameShareApp)
        val frameSupportContact = findViewById<FrameLayout>(R.id.frameSupportContact)
        val frameUserLicense = findViewById<FrameLayout>(R.id.frameUserLicense)
        val switchDarkTheme = findViewById<SwitchMaterial>(R.id.switchDarkTheme)
        // GoBack button
        tvGoBack.setOnClickListener { finish() }
        // DarkTheme switch
        switchDarkTheme.isChecked = (applicationContext as App).isDarkTheme
        switchDarkTheme.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }
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

}