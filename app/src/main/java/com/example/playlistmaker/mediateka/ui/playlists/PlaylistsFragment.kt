package com.example.playlistmaker.mediateka.ui.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewmodel: PlaylistsViewModel by viewModel()
    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObservers()
    }

    private fun initializeObservers() {
        viewmodel.screenState.observe(viewLifecycleOwner) {screenState ->
            when(screenState) {
                is PlaylistsScreenState.Content -> showContent(screenState)
                PlaylistsScreenState.Error -> TODO()
                PlaylistsScreenState.Loading -> TODO()
            }
        }
    }

    private fun showContent(screenState: PlaylistsScreenState.Content) {
        if (screenState.playlistsList.isEmpty()) binding.lrPlaceHolder.isVisible = true
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}