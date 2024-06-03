package com.example.playlistmaker.player.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistSmallItemBinding
import com.example.playlistmaker.mediateka.domain.models.Playlist

class PlaylistsSmallAdapter(val onClick: (Playlist) -> Unit) :
    RecyclerView.Adapter<PlaylistSmallViewHolder>() {

    var playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSmallViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistSmallViewHolder(
            PlaylistSmallItemBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistSmallViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { onClick(playlists[position]) }
    }

}