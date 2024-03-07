package com.example.playlistmaker.mediateka.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMediatekaBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediatekaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediatekaBinding
    private val viewModel: MediatekaViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}