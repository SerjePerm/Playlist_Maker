package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.Constants.Companion.TRACK_EXTRA
import com.example.playlistmaker.utils.dpToPx
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private lateinit var viewModel: PlayerViewModel

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        track = intent.getSerializableExtra(TRACK_EXTRA) as Track
        viewModel = ViewModelProvider(this, PlayerViewModel.getViewModelFactory(track.previewUrl)
        )[PlayerViewModel::class.java]
        initializeClickListeners()
        initializeObservers()
        setTrackDataToViews(track)
    }

    private fun initializeClickListeners() {
        binding.ivBackButton.setOnClickListener { finish() }
        binding.ivPlayButton.setOnClickListener { viewModel.playPauseClick() }
    }

    private fun initializeObservers() {
        //Player state observer
        viewModel.playerState.observe(this@PlayerActivity) { playerState ->
            if (playerState == PlayerState.PLAYING) {
                binding.ivPlayButton.setImageResource(R.drawable.pause)
            } else {
                binding.ivPlayButton.setImageResource(R.drawable.play)
            }
        }
        //Player position observer
        viewModel.playerPos.observe(this@PlayerActivity) { playerPos ->
            binding.tvPlayTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerPos)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onActivityPaused()
    }

    private fun setTrackDataToViews(track: Track) {
        binding.tvTrackName.text = track.trackName
        binding.tvArtistName.text = track.artistName
        binding.tvTrackTime.text = track.trackTime
        binding.tvCollection.text = track.collectionName
        binding.tvYear.text = track.releaseYear
        binding.tvGenre.text = track.primaryGenreName
        binding.tvCountry.text = track.country
        Glide.with(this).load(track.bigCoverUrl).placeholder(R.drawable.placeholder_big)
            .centerCrop().transform(RoundedCorners(dpToPx(8, this))).into(binding.ivPoster)
    }

}