package com.example.playlistmaker.mediateka.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistBigItemBinding
import com.example.playlistmaker.mediateka.domain.models.Playlist

class PlaylistsBigAdapter(val onClick: (Playlist) -> Unit) :
    RecyclerView.Adapter<PlaylistBigViewHolder>() {

    var playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistBigViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistBigViewHolder(PlaylistBigItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistBigViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { onClick(playlists[position]) }
    }

}