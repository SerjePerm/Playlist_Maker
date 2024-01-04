package com.example.playlistmaker.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.activities.SearchActivity.Companion.trackExtra
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.utils.dpToPx
import com.example.playlistmaker.utils.msToTime

class PlayerActivity : AppCompatActivity() {

    lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        //view val
        val ivBackButton = findViewById<ImageView>(R.id.ivBackButton)
        val tvTrackName = findViewById<TextView>(R.id.tvTrackName)
        val tvArtistName = findViewById<TextView>(R.id.tvArtistName)
        val tvTrackTime = findViewById<TextView>(R.id.tvTrackTime)
        val tvCollection = findViewById<TextView>(R.id.tvCollection)
        val tvYear = findViewById<TextView>(R.id.tvYear)
        val tvGenre = findViewById<TextView>(R.id.tvGenre)
        val tvCountry = findViewById<TextView>(R.id.tvCountry)
        val ivPoster = findViewById<ImageView>(R.id.ivPoster)
        //GoBack button
        ivBackButton.setOnClickListener { finish() }
        //load track from extra and set all textViews
        track = intent.getSerializableExtra(trackExtra) as Track
        tvTrackName.text = track.trackName
        tvArtistName.text = track.artistName
        tvTrackTime.text = msToTime(track.trackTimeMillis)
        tvCollection.text = track.collectionName
        tvYear.text = track.releaseDate.substringBefore('-')
        tvGenre.text = track.primaryGenreName
        tvCountry.text = track.country
        //load poster
        Glide.with(this)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_big)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8, this)))
            .into(ivPoster)
    }
}