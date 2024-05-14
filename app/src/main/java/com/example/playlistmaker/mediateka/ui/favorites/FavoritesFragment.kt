package com.example.playlistmaker.mediateka.ui.favorites

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.adapters.SearchTracksAdapter
import com.example.playlistmaker.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val viewmodel: FavoritesViewModel by viewModel()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val favoriteTrackList = ArrayList<Track>()
    private lateinit var favoriteTracksAdapter: SearchTracksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObservers()
        initializeAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeObservers() {
        viewmodel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is FavoritesScreenState.Content -> showContent(screenState)
                FavoritesScreenState.Error -> {}
                FavoritesScreenState.Loading -> {}
            }
        }
    }

    private fun initializeAdapter() {
        favoriteTracksAdapter = SearchTracksAdapter { track ->
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            intent.putExtra(Constants.TRACK_EXTRA, track)
            startActivity(intent)
        }
        favoriteTracksAdapter.tracks = favoriteTrackList
        binding.rvTracksFavorites.adapter = favoriteTracksAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(screenState: FavoritesScreenState.Content) {
        if (screenState.favoritesList.isNotEmpty()) {
            favoriteTrackList.clear()
            favoriteTrackList.addAll(screenState.favoritesList)
            favoriteTracksAdapter.notifyDataSetChanged()
            binding.lrPlaceHolder.isVisible = false
        } else {
            favoriteTrackList.clear()
            favoriteTracksAdapter.notifyDataSetChanged()
            binding.lrPlaceHolder.isVisible = true
        }
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }

}