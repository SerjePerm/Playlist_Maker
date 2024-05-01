package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isDarkTheme.observe(viewLifecycleOwner) { binding.switchDarkTheme.isChecked = it }
        initializeClickListeners()
    }

    private fun initializeClickListeners() {
        binding.switchDarkTheme.setOnCheckedChangeListener { _, value -> viewModel.changeTheme(value) }
        binding.frameShareApp.setOnClickListener { viewModel.shareApp() }
        binding.frameSupportContact.setOnClickListener { viewModel.supportContact() }
        binding.frameUserLicense.setOnClickListener { viewModel.userLicense() }
    }

}