package com.example.playlistmaker.mediateka.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.adapters.TrackViewHolder

class BSTracksAdapter(
    val onClick: (Track) -> Unit,
    val onLongClick: (Track) -> Boolean
) : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { onClick(tracks[position]) }
        holder.itemView.setOnLongClickListener { onLongClick(tracks[position]) }
    }

}