package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.isDarkTheme.observe(this) { binding.switchDarkTheme.isChecked = it }
        initializeClickListeners()
    }

    private fun initializeClickListeners() {
        binding.tvGoBack.setOnClickListener { finish() }
        binding.switchDarkTheme.setOnCheckedChangeListener { _, value -> viewModel.changeTheme(value) }
        binding.frameShareApp.setOnClickListener{ viewModel.shareApp() }
        binding.frameSupportContact.setOnClickListener { viewModel.supportContact() }
        binding.frameUserLicense.setOnClickListener { viewModel.userLicense() }
    }

}