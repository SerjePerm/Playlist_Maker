package com.example.playlistmaker.root.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcvContainer) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnvBottomNav.setupWithNavController(navController)
        //show/hide bottom nav
        val hideList = listOf(R.id.playerFragment, R.id.addPlaylistFragment, R.id.playlistFragment)
        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            binding.bnvBottomNav.isVisible = !hideList.contains(navDestination.id)
        }
    }

}