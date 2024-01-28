package com.example.playlistmaker.activities

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.activities.SearchActivity.Companion.TRACK_EXTRA
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.utils.dpToPx
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private var mainThreadHandler: Handler? = null
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //GoBack button
        binding.ivBackButton.setOnClickListener { finish() }
        //load track from extra and set all textViews
        track = intent.getSerializableExtra(TRACK_EXTRA) as Track
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
        //media player
        preparePlayer(track.previewUrl)
        binding.ivPlayButton.setOnClickListener { playClick() }
        mainThreadHandler = Handler(Looper.getMainLooper())
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.ivPlayButton.setImageResource(R.drawable.play)
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.ivPlayButton.setImageResource(R.drawable.play)
            binding.tvPlayTime.setText(R.string.zero_time)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.ivPlayButton.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.ivPlayButton.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
    }

    private fun playClick() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startTimer() {
        mainThreadHandler?.post(
            createUpdateTimerTask()
        )
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    binding.tvPlayTime.text = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                    mainThreadHandler?.postDelayed(this, DELAY)
                }
            }
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 500L
    }
}