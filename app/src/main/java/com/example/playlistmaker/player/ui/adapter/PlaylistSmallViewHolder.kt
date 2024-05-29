package com.example.playlistmaker.player.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistSmallItemBinding
import com.example.playlistmaker.mediateka.domain.models.Playlist

class PlaylistSmallViewHolder(private val binding: PlaylistSmallItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        binding.tvTitle.text = playlist.title
        binding.tvCount.text = playlist.count.toString()
        /*
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2, itemView.context)))
            .into(binding.ivCover)
         */
    }

}