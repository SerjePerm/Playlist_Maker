package com.example.playlistmaker.mediateka.ui.editplaylist

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.example.playlistmaker.mediateka.domain.models.Playlist
import com.example.playlistmaker.utils.Constants.Companion.PLAYLIST_EXTRA
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : Fragment() {

    private val viewmodel: EditPlaylistViewModel by viewModel()
    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(PLAYLIST_EXTRA, Playlist::class.java) as Playlist
        } else requireArguments().getSerializable(PLAYLIST_EXTRA) as Playlist
        viewmodel.setPlaylistToState(playlist)
        initializeData(playlist)
        initializeObservers()
        initializePickMedia()
        initializeClickListeners()
        initializeTextChangedListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeData(playlist: Playlist) {
        binding.tvGoBack.text = getString(R.string.edit_title)
        binding.btnCreate.text = getString(R.string.save_btn)
        binding.etTitle.setText(playlist.title)
        binding.etDescription.setText(playlist.description)
    }

    private fun initializeObservers() {
        viewmodel.screenState.observe(viewLifecycleOwner) { screenState ->
            binding.btnCreate.isEnabled = screenState.isTitleNotEmpty
            if (screenState.poster != null) {
                binding.ivSelectImage.setImageURI(screenState.poster)
                binding.ivSelectImage.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }
    }

    private fun initializePickMedia() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewmodel.changeUri(uri = uri)
            } else {
                showToast(getString(R.string.image_empty))
            }
        }
    }

    private fun initializeClickListeners() {
        binding.ivSelectImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }
        binding.btnCreate.setOnClickListener {
            viewmodel.saveClick()
            showToast(getString(R.string.playlist_edited, binding.etTitle.text))
            findNavController().navigateUp()
        }
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
        binding.tvGoBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun initializeTextChangedListener() {
        binding.etTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewmodel.changeTitle(s.toString())
            }
        })
        binding.etDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewmodel.changeDescription(s.toString())
            }
        })
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun createArguments(playlist: Playlist): Bundle = bundleOf(PLAYLIST_EXTRA to playlist)
    }

}