package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.mediateka.domain.models.Playlist
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.player.ui.adapter.PlaylistsSmallAdapter
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.Constants.Companion.TRACK_EXTRA
import com.example.playlistmaker.utils.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel()
    private val playlistsList = ArrayList<Playlist>()
    private lateinit var playlistsAdapter: PlaylistsSmallAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        track = intent.getSerializableExtra(TRACK_EXTRA) as Track
        viewModel.setDataSource(track)
        initializeClickListeners()
        initializeObservers()
        initializeAdapter()
        initializeBottomSheet()
        setTrackDataToViews(track)
    }

    private fun initializeClickListeners() {
        binding.ivBackButton.setOnClickListener { finish() }
        binding.ivPlayButton.setOnClickListener { viewModel.playPauseClick() }
        binding.ibFavoriteButton.setOnClickListener { viewModel.changeFavoriteClick(track) }
        binding.ibAddButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initializeObservers() {
        viewModel.screenState.observe(this@PlayerActivity) { screenState ->
            when (screenState) {
                PlayerScreenState.Error -> {}
                PlayerScreenState.Loading -> {}
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
        viewModel.playlists.observe(this@PlayerActivity) { list ->
            playlistsList.clear()
            playlistsList.addAll(list)
            playlistsAdapter.notifyDataSetChanged()
        }
    }

    private fun initializeAdapter() {
        playlistsAdapter = PlaylistsSmallAdapter { playlist ->
            viewModel.addTrackToPlaylist(track = track, playlist = playlist)
        }
        playlistsAdapter.playlists = playlistsList
        binding.rvPlaylists.adapter = playlistsAdapter
    }

    private fun initializeBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.lrBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.visibility = View.GONE
                        }

                        else -> {
                            binding.overlay.visibility = View.VISIBLE
                        }
                    }
                }
            }
        )
    }

    private fun updatePlayerInfo(playerState: PlayerState, playerPos: Int) {
        when (playerState) {
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
                binding.tvPlayTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerPos)
            }

            PlayerState.PAUSED -> {
                binding.ivPlayButton.setImageResource(R.drawable.play)
                binding.tvPlayTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerPos)
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