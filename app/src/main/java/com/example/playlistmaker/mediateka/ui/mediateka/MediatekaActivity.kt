package com.example.playlistmaker.mediateka.ui.mediateka

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediatekaBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediatekaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediatekaBinding
    private val viewModel: MediatekaViewModel by viewModel()
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeClickListeners()
        initializeFragmentsTabs()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

    private fun initializeClickListeners() {
        binding.tvGoBack.setOnClickListener { finish() }
    }

    private fun initializeFragmentsTabs() {
        binding.viewPager.adapter = PagerAdapter(supportFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.favorites)
                1 -> tab.text = resources.getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

}