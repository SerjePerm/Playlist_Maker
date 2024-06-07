package com.example.playlistmaker.mediateka.ui.playlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.mediateka.ui.adapter.BSTracksAdapter
import com.example.playlistmaker.mediateka.ui.editplaylist.EditPlaylistFragment
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.Constants.Companion.PLAYLIST_ID
import com.example.playlistmaker.utils.toTracksCount
import com.example.playlistmaker.utils.toTracksLength
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private val viewmodel: PlaylistViewModel by viewModel()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val trackList = ArrayList<Track>()
    private lateinit var tracksAdapter: BSTracksAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistId = requireArguments().getInt(PLAYLIST_ID, 0)
        viewmodel.setPlaylistId(playlistId)
        initializeObservers()
        initializeAdapter()
        initializeClickListeners()
        initializeBottomSheet()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initializeObservers() {
        viewmodel.screenState.observe(viewLifecycleOwner) { screenState ->
            with(screenState.playlist) {
                binding.tvPlaylistTitle.text = title
                binding.tvPlaylistTitleBS.text = title
                binding.tvDescription.text = description
                binding.tvLength.text = screenState.tracksLength.toTracksLength()
                binding.tvTracksCount.text = count.toTracksCount()
                binding.tvTrackCountBS.text = count.toTracksCount()
                if (poster == null) {
                    binding.ivSmallPoster.setImageResource(R.drawable.placeholder)
                    binding.ivPosterBack.setImageResource(R.drawable.placeholder)
                } else {
                    binding.ivSmallPoster.setImageURI(poster)
                    binding.ivPosterBack.setImageURI(poster)
                }
            }
            with(screenState.trackList) {
                if (isNotEmpty()) {
                    trackList.clear()
                    trackList.addAll(this)
                    tracksAdapter.notifyDataSetChanged()
                    binding.tvPlaceholder.isVisible = false
                } else {
                    trackList.clear()
                    tracksAdapter.notifyDataSetChanged()
                    binding.tvPlaceholder.isVisible = true
                }
            }
        }
    }

    private fun initializeAdapter() {
        val onClick: (Track) -> Unit = { track ->
            findNavController().navigate(
                resId = R.id.action_playlistFragment_to_playerFragment,
                args = PlayerFragment.createArguments(track)
            )
        }
        val onLongClick: (Track) -> Boolean = { track ->
            trackDelConfirm(track)
            true
        }
        tracksAdapter = BSTracksAdapter(onClick = onClick, onLongClick = onLongClick)
        tracksAdapter.tracks = trackList
        binding.rvTracks.adapter = tracksAdapter
    }

    private fun initializeClickListeners() {
        with(binding) {
            ivMenu.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            ivBackBtn.setOnClickListener { findNavController().navigateUp() }
            ivShare.setOnClickListener { shareClick() }
            tvPlShare.setOnClickListener { shareClick() }
            tvPlDelete.setOnClickListener { playlistDelConfirm() }
            tvPlEdit.setOnClickListener {
                findNavController().navigate(
                    resId = R.id.action_playlistFragment_to_editPlaylistFragment,
                    args = EditPlaylistFragment.createArguments(viewmodel.screenState.value!!.playlist)
                )
            }
        }
    }

    private fun initializeBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bsMenu)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.vOverlay.visibility = View.GONE
                    }
                    else -> {
                        binding.vOverlay.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun trackDelConfirm(track: Track) {
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog)
            .setTitle(getString(R.string.del_track_title))
            .setMessage(getString(R.string.del_track_text))
            .setNeutralButton(getString(R.string.del_track_cancel)) { _, _ ->
                //close dialog
            }.setPositiveButton(R.string.del_track_confirm) { _, _ ->
                viewmodel.delTrack(track)
            }.show()
    }

    private fun playlistDelConfirm() {
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog)
            .setTitle(getString(R.string.del_pl_title))
            .setMessage(getString(R.string.del_pl_text))
            .setNeutralButton(getString(R.string.del_pl_cancel)) { _, _ ->
                //close dialog
            }.setPositiveButton(R.string.del_track_confirm) { _, _ ->
                findNavController().navigateUp()
                viewmodel.delPlaylist()
            }.show()
    }

    private fun shareClick() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        if (trackList.isNotEmpty()) {
            viewmodel.sharePlaylist()
        }
        else Toast.makeText(requireContext(), R.string.no_tracks, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun createArguments(playlistId: Int): Bundle = bundleOf(PLAYLIST_ID to playlistId)
    }

}