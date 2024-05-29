package com.example.playlistmaker.mediateka.ui.playlists

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.mediateka.domain.models.Playlist
import com.example.playlistmaker.mediateka.ui.adapter.PlaylistsBigAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewmodel: PlaylistsViewModel by viewModel()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val playlistsList = ArrayList<Playlist>()
    private lateinit var playlistsAdapter: PlaylistsBigAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObservers()
        initializeListeners()
        initializeAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeObservers() {
        viewmodel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is PlaylistsScreenState.Content -> showContent(screenState)
                PlaylistsScreenState.Error -> {}
                PlaylistsScreenState.Loading -> {}
            }
        }
    }

    private fun initializeListeners() {
        binding.btnAddPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediatekaFragment_to_addPlaylistFragment)
        }
    }

    private fun initializeAdapter() {
        playlistsAdapter = PlaylistsBigAdapter { playlist ->
            println("clicked ${playlist.title}")
        }
        playlistsAdapter.playlists = playlistsList
        binding.rvPlaylists.adapter = playlistsAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(screenState: PlaylistsScreenState.Content) {
        if (screenState.playlistsList.isNotEmpty()) {
            binding.lrPlaceHolder.isVisible = false
            playlistsList.clear()
            playlistsList.addAll(screenState.playlistsList)
            playlistsAdapter.notifyDataSetChanged()
        } else {
            binding.lrPlaceHolder.isVisible = true
            playlistsList.clear()
            playlistsAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}