package com.example.playlistmaker.presentation.ui.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.utils.dpToPx

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val ivCover: ImageView = itemView.findViewById(R.id.ivCover)
    private val tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.tvTrackTime)

    fun bind(track: Track) {
        tvTrackName.text = track.trackName
        tvArtistName.text = track.artistName
        tvTrackTime.text = track.trackTime
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2, itemView.context)))
            .into(ivCover)
    }
}