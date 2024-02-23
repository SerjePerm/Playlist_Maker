package com.example.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.mediateka.ui.MediatekaActivity
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, MainViewModel.getViewModelFactory())[MainViewModel::class.java]
        viewModel.navigatePath.observe(this) { navigate(it) }
        initializeClickListeners()
    }

    private fun initializeClickListeners() {
        binding.btnSearch.setOnClickListener { viewModel.searchClick() }
        binding.btnMediateka.setOnClickListener { viewModel.mediatekaClick() }
        binding.btnSettings.setOnClickListener { viewModel.settingsClick() }
    }

    private fun navigate(path: MainPaths) {
        when(path) {
            MainPaths.NONE -> { }
            MainPaths.SEARCH -> {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
            MainPaths.MEDIATEKA -> {
                val intent = Intent(this, MediatekaActivity::class.java)
                startActivity(intent)
            }
            MainPaths.SETTINGS -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
    }

}