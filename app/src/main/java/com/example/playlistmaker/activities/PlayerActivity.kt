package com.example.playlistmaker.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.activities.SearchActivity.Companion.trackExtra
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.utils.dpToPx

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //GoBack button
        binding.ivBackButton.setOnClickListener { finish() }
        //load track from extra and set all textViews
        track = intent.getSerializableExtra(trackExtra) as Track
        binding.tvTrackName.text = track.trackName
        binding.tvArtistName.text = track.artistName
        binding.tvTrackTime.text = track.trackTime
        binding.tvCollection.text = track.collectionName
        binding.tvYear.text = track.releaseYear
        binding.tvGenre.text = track.primaryGenreName
        binding.tvCountry.text = track.country
        //load poster
        Glide.with(this)
            .load(track.bigCoverUrl)
            .placeholder(R.drawable.placeholder_big)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8, this)))
            .into(binding.ivPoster)
    }
}