package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.Constants.Companion.TRACK_EXTRA
import com.example.playlistmaker.utils.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel()

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        track = intent.getSerializableExtra(TRACK_EXTRA) as Track
        viewModel.setDataSource(track)
        initializeClickListeners()
        initializeObservers()
        setTrackDataToViews(track)
    }

    private fun initializeClickListeners() {
        binding.ivBackButton.setOnClickListener { finish() }
        binding.ivPlayButton.setOnClickListener { viewModel.playPauseClick() }
        binding.ibFavoriteButton.setOnClickListener { viewModel.changeFavoriteClick(track) }
    }

    private fun initializeObservers() {
        viewModel.screenState.observe(this@PlayerActivity) { screenState ->
            when (screenState) {
                PlayerScreenState.Error -> { }
                PlayerScreenState.Loading -> { }
                is PlayerScreenState.Content -> {
                    updatePlayerInfo(
                        playerState = screenState.playerState,
                        playerPos = screenState.playerPos
                    )
                }
            }
        }
        viewModel.isFavorite.observe(this@PlayerActivity) { isFavorite ->
            if (isFavorite) binding.ibFavoriteButton.setImageResource(R.drawable.del_from_favorite)
            else binding.ibFavoriteButton.setImageResource(R.drawable.add_to_favorite)
        }
    }

    private fun updatePlayerInfo(playerState: PlayerState, playerPos: Int) {
        when(playerState) {
            PlayerState.DEFAULT -> {
                binding.ivPlayButton.setImageResource(R.drawable.play)
                binding.tvPlayTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
            }
            PlayerState.PREPARED -> {
                binding.ivPlayButton.setImageResource(R.drawable.play)
                binding.tvPlayTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
            }
            PlayerState.PLAYING -> {
                binding.ivPlayButton.setImageResource(R.drawable.pause)
                binding.tvPlayTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerPos)
            }
            PlayerState.PAUSED -> {
                binding.ivPlayButton.setImageResource(R.drawable.play)
                binding.tvPlayTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerPos)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onActivityPaused()
    }

    private fun setTrackDataToViews(track: Track) {
        with(binding) {
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName
            tvTrackTime.text = track.trackTime
            tvCollection.text = track.collectionName
            tvYear.text = track.releaseYear
            tvGenre.text = track.primaryGenreName
            tvCountry.text = track.country
        }
        Glide.with(this).load(track.bigCoverUrl).placeholder(R.drawable.placeholder_big)
            .centerCrop().transform(RoundedCorners(dpToPx(8, this))).into(binding.ivPoster)
    }

}