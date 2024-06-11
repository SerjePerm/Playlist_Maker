package com.example.playlistmaker.mediateka.ui.editplaylist

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.mediateka.domain.models.Playlist
import com.example.playlistmaker.mediateka.ui.addplaylist.AddPlaylistFragment
import com.example.playlistmaker.utils.Constants.Companion.PLAYLIST_EXTRA
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : AddPlaylistFragment() {

    override val viewmodel: EditPlaylistViewModel by viewModel()
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().navigateUp()
        }
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(PLAYLIST_EXTRA, Playlist::class.java) as Playlist
        } else requireArguments().getSerializable(PLAYLIST_EXTRA) as Playlist
        viewmodel.setPlaylistToState(playlist)
        initializeData(playlist)
        initializeObservers()
        initializeClickListeners()
        super.initializePickMedia()
        super.initializeTextChangedListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback.remove()
    }

    private fun initializeData(playlist: Playlist) {
        binding.tvGoBack.text = getString(R.string.edit_title)
        binding.btnCreate.text = getString(R.string.save_btn)
        binding.etTitle.setText(playlist.title)
        binding.etDescription.setText(playlist.description)
    }

    private fun initializeObservers() {
        viewmodel.state.observe(viewLifecycleOwner) { screenState ->
            binding.btnCreate.isEnabled = screenState.isTitleNotEmpty
            if (screenState.poster != null) {
                binding.ivSelectImage.setImageURI(screenState.poster)
                binding.ivSelectImage.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }
    }

    private fun initializeClickListeners() {
        binding.ivSelectImage.setOnClickListener {
            pickMedia?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }
        binding.btnCreate.setOnClickListener {
            viewmodel.saveClick()
            showToast(getString(R.string.playlist_edited, binding.etTitle.text))
            findNavController().navigateUp()
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
        binding.tvGoBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun createArguments(playlist: Playlist): Bundle = bundleOf(PLAYLIST_EXTRA to playlist)
    }

}