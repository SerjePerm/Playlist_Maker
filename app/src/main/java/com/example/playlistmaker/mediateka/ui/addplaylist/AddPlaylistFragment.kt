package com.example.playlistmaker.mediateka.ui.addplaylist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class AddPlaylistFragment : Fragment() {

    private val viewmodel: AddPlaylistViewModel by viewModel()
    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var isChangesExist: Boolean = false

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
        _binding = null
    }

    private fun initializeObservers() {
        viewmodel.screenState.observe(viewLifecycleOwner) { screenState ->
            //println("isChangesExist: ${screenState.isChangesExist}")
            val bool = screenState.isTitleNotEmpty
            println("on fragment bool: ${bool}")
            binding.btnCreate.isEnabled = bool

            isChangesExist = screenState.isChangesExist
            binding.ivSelectImage.setImageURI(screenState.uri)
        }
    }

    private fun initializePickMedia() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewmodel.changeUri(uri = uri)
            } else {
                Toast.makeText(requireContext(), getString(R.string.image_empty), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun initializeClickListeners() {
        binding.ivSelectImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }
        binding.tvGoBack.setOnClickListener { showExitDialog() }
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        })
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

    private fun showExitDialog() {
        MaterialAlertDialogBuilder(requireActivity()).setTitle(getString(R.string.dialog_title))
            .setMessage(getString(R.string.dialog_text))
            .setNeutralButton(getString(R.string.dialog_cancel)) { _, _ ->
                //close dialog
            }.setPositiveButton(R.string.dialog_ok) { _, _ ->
                findNavController().navigateUp()
            }.show()
    }

}