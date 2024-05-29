package com.example.playlistmaker.mediateka.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistBigItemBinding
import com.example.playlistmaker.mediateka.domain.models.Playlist

class PlaylistBigViewHolder(private val binding: PlaylistBigItemBinding) :
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