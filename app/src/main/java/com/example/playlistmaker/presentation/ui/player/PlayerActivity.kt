package com.example.playlistmaker.presentation.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.presenters.mediaplayer.MediaPlayerPresenter
import com.example.playlistmaker.presentation.presenters.mediaplayer.MediaPlayerView
import com.example.playlistmaker.presentation.ui.search.SearchActivity.Companion.TRACK_EXTRA
import com.example.playlistmaker.utils.dpToPx
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity(), MediaPlayerView {

    private var mainThreadHandler: Handler? = null
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private lateinit var presenter: MediaPlayerPresenter

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        track = intent.getSerializableExtra(TRACK_EXTRA) as Track
        presenter = Creator.provideMediaPlayerPresenter(this, track.previewUrl)
        mainThreadHandler = Handler(Looper.getMainLooper())
        //
        setTrackDataToViews(track)
        initializeClickListeners()
        //load poster
        Glide.with(this).load(track.bigCoverUrl).placeholder(R.drawable.placeholder_big).centerCrop().transform(RoundedCorners(dpToPx(8, this))).into(binding.ivPoster)
    }

    private fun initializeClickListeners() {
        //GoBack button
        binding.ivBackButton.setOnClickListener { finish() }
        //media player
        binding.ivPlayButton.setOnClickListener { presenter.playPauseClick() }
    }

    override fun onPause() {
        super.onPause()
        presenter.onActivityPaused()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onActivityDestroyed()
    }

    private fun setTrackDataToViews(track: Track) {
        binding.tvTrackName.text = track.trackName
        binding.tvArtistName.text = track.artistName
        binding.tvTrackTime.text = track.trackTime
        binding.tvCollection.text = track.collectionName
        binding.tvYear.text = track.releaseYear
        binding.tvGenre.text = track.primaryGenreName
        binding.tvCountry.text = track.country
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (presenter.getState() == PlayerState.PLAYING) {
                    binding.tvPlayTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(presenter.getPos())
                    mainThreadHandler?.postDelayed(this, DELAY)
                }
            }
        }
    }

    override fun setState(playerState: PlayerState) {
        if (playerState == PlayerState.PLAYING) {
            mainThreadHandler?.post(createUpdateTimerTask())
            binding.ivPlayButton.setImageResource(R.drawable.pause)
        } else binding.ivPlayButton.setImageResource(R.drawable.play)
    }

    companion object {
        private const val DELAY = 500L
    }
}