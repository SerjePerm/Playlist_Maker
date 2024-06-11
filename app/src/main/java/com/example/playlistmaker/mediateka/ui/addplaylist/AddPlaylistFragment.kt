package com.example.playlistmaker.mediateka.ui.addplaylist

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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

open class AddPlaylistFragment : Fragment() {

    open val viewmodel: AddPlaylistViewModel by viewModel()
    open var _binding: FragmentAddPlaylistBinding? = null
    open val binding get() = _binding!!
    open var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null
    private var isChangesExist: Boolean = false
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showExitDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObservers()
        initializePickMedia()
        initializeClickListeners()
        initializeTextChangedListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback.remove()
        _binding = null
    }

    private fun initializeObservers() {
        viewmodel.screenState.observe(viewLifecycleOwner) { screenState ->
            //empty title
            binding.btnCreate.isEnabled = screenState.isTitleNotEmpty
            //changes exist for exit dialog
            isChangesExist = screenState.isChangesExist
            //select image
            if (screenState.uri != null) {
                binding.ivSelectImage.setImageURI(screenState.uri)
                binding.ivSelectImage.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }
    }

    fun initializePickMedia() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewmodel.changeUri(uri = uri)
            } else {
                showToast(getString(R.string.image_empty))
            }
        }
    }

    private fun initializeClickListeners() {
        //select image
        binding.ivSelectImage.setOnClickListener {
            pickMedia?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }
        //create playlist
        binding.btnCreate.setOnClickListener {
            viewmodel.createClick()
            showToast(getString(R.string.playlist_created, binding.etTitle.text))
            findNavController().navigateUp()
        }
        //title with back
        binding.tvGoBack.setOnClickListener { showExitDialog() }
        //system back button
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }

    open fun initializeTextChangedListener() {
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

    private fun showExitDialog() {
        if (isChangesExist) {
            MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog)
                .setTitle(getString(R.string.dialog_title))
                .setMessage(getString(R.string.dialog_text))
                .setNeutralButton(getString(R.string.dialog_cancel)) { _, _ ->
                    //close dialog
                }.setPositiveButton(R.string.dialog_ok) { _, _ ->
                    findNavController().navigateUp()
                }.show()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

}