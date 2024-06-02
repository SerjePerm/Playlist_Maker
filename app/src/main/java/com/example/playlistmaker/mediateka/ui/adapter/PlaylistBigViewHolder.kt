package com.example.playlistmaker.mediateka.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistBigItemBinding
import com.example.playlistmaker.mediateka.domain.models.Playlist
import com.example.playlistmaker.utils.toTracksCount

class PlaylistBigViewHolder(private val binding: PlaylistBigItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        binding.tvTitle.text = playlist.title
        binding.tvCount.text = playlist.count.toTracksCount()
        if (playlist.poster==null) binding.ivPoster.setImageResource(R.drawable.placeholder)
        else binding.ivPoster.setImageURI(playlist.poster)
    }

}