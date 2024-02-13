package com.example.playlistmaker.presentation.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeClickListeners()
    }

    private fun initializeClickListeners() {
        // GoBack button
        binding.tvGoBack.setOnClickListener { finish() }
        // DarkTheme switch
        binding.switchDarkTheme.isChecked = (applicationContext as App).isDarkTheme
        binding.switchDarkTheme.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }
        // ShareApp button
        binding.frameShareApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
            startActivity(intent)
        }
        // SupportContact button
        binding.frameSupportContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse(getString(R.string.support_adress))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_text))
            startActivity(intent)
        }
        // UserLicense button
        binding.frameUserLicense.setOnClickListener {
            val url = Uri.parse(getString(R.string.user_license_URL))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
    }

}