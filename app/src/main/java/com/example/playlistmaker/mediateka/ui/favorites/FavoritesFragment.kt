package com.example.playlistmaker.mediateka.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val viewmodel: FavoritesViewModel by viewModel()
    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObservers()
    }

    private fun initializeObservers() {
        viewmodel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is FavoritesScreenState.Content -> showContent(screenState)
                FavoritesScreenState.Error -> TODO()
                FavoritesScreenState.Loading -> TODO()
            }
        }
    }

    private fun showContent(screenState: FavoritesScreenState.Content) {
        if (screenState.favoritesList.isEmpty()) binding.lrPlaceHolder.isVisible = true
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }

}