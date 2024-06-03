package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.mediateka.domain.models.Playlist
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.player.ui.adapter.PlaylistsSmallAdapter
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.Constants.Companion.TRACK_EXTRA
import com.example.playlistmaker.utils.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel()
    private val playlistsList = ArrayList<Playlist>()
    private lateinit var playlistsAdapter: PlaylistsSmallAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(TRACK_EXTRA, Track::class.java) as Track
        } else requireArguments().getSerializable(TRACK_EXTRA) as Track
        viewModel.setDataSource(track)
        initializeClickListeners()
        initializeObservers()
        initializeAdapter()
        initializeBottomSheet()
        setTrackDataToViews(track)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeClickListeners() {
        binding.ivBackButton.setOnClickListener { findNavController().navigateUp() }
        binding.ivPlayButton.setOnClickListener { viewModel.playPauseClick() }
        binding.ibFavoriteButton.setOnClickListener { viewModel.changeFavoriteClick(track) }
        binding.ibAddButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        binding.btnNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_addPlaylistFragment)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initializeObservers() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                PlayerScreenState.Error -> {}
                PlayerScreenState.Loading -> {}
                is PlayerScreenState.Content -> {
                    updatePlayerInfo(
                        playerState = screenState.playerState,
                        playerPos = screenState.playerPos
                    )
                }
            }
        }
        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) binding.ibFavoriteButton.setImageResource(R.drawable.del_from_favorite)
            else binding.ibFavoriteButton.setImageResource(R.drawable.add_to_favorite)
        }
        viewModel.playlists.observe(viewLifecycleOwner) { list ->
            playlistsList.clear()
            playlistsList.addAll(list)
            playlistsAdapter.notifyDataSetChanged()
        }
        viewModel.addResult.observe(viewLifecycleOwner) { addResult ->
            if (addResult.successful==true) {
                showToast(getString(R.string.track_added, addResult.playlist))
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
            else if (addResult.successful==false) {
                showToast(getString(R.string.track_exist, addResult.playlist))
            }
        }
    }

    private fun initializeAdapter() {
        playlistsAdapter = PlaylistsSmallAdapter { playlist ->
            viewModel.addTrackToPlaylist(track = track, playlist = playlist)
        }
        playlistsAdapter.playlists = playlistsList
        binding.rvPlaylists.adapter = playlistsAdapter
    }

    private fun initializeBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.lrBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.visibility = View.GONE
                        }

                        else -> {
                            binding.overlay.visibility = View.VISIBLE
                        }
                    }
                }
            }
        )
    }

    private fun updatePlayerInfo(playerState: PlayerState, playerPos: Int) {
        when (playerState) {
            PlayerState.DEFAULT -> {
                binding.ivPlayButton.setImageResource(R.drawable.play)
                binding.tvPlayTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
            }

            PlayerState.PREPARED -> {
                binding.ivPlayButton.setImageResource(R.drawable.play)
                binding.tvPlayTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
            }

            PlayerState.PLAYING -> {
                binding.ivPlayButton.setImageResource(R.drawable.pause)
                binding.tvPlayTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerPos)
            }

            PlayerState.PAUSED -> {
                binding.ivPlayButton.setImageResource(R.drawable.play)
                binding.tvPlayTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerPos)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onFragmentPaused()
    }

    private fun setTrackDataToViews(track: Track) {
        with(binding) {
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName
            tvTrackTime.text = track.trackTime
            tvCollection.text = track.collectionName
            tvYear.text = track.releaseYear
            tvGenre.text = track.primaryGenreName
            tvCountry.text = track.country
        }
        Glide.with(this).load(track.bigCoverUrl).placeholder(R.drawable.placeholder_big)
            .centerCrop().transform(RoundedCorners(dpToPx(8, requireContext()))).into(binding.ivPoster)
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun createArguments(track: Track): Bundle = bundleOf(TRACK_EXTRA to track)
    }

}