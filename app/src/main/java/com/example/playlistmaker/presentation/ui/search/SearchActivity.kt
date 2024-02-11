package com.example.playlistmaker.presentation.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker.Creator
import com.example.playlistmaker.PREFERENCES_TITLE
import com.example.playlistmaker.R
import com.example.playlistmaker.SEARCH_HISTORY
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.PlaceHolderType
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TracksResponse
import com.example.playlistmaker.presentation.ui.player.PlayerActivity

class SearchActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var binding: ActivitySearchBinding
    private var searchText = ""

    //for search results
    private val trackList = ArrayList<Track>()
    private lateinit var trackAdapter: TrackAdapter
    private var isClickAllowed = true
    private val searchRunnable = Runnable { search(binding.etSearch.text.toString()) }
    private val clickRunnable = Runnable { isClickAllowed = true }

    //for search history
    private lateinit var listener: OnSharedPreferenceChangeListener

    // TracksInteractor + consumer
    private lateinit var tracksInteractor: TracksInteractor
    private val consumer = object : TracksInteractor.TracksConsumer {
        @SuppressLint("NotifyDataSetChanged")
        override fun consume(tracksResponse: TracksResponse) {
            handler.post {
                binding.progressBar.visibility = View.GONE
                binding.rvTracksSearch.visibility = View.VISIBLE
                if (tracksResponse.resultCode == 200) {
                    trackList.clear()
                    if (tracksResponse.results.isNotEmpty()) {
                        trackList.addAll(tracksResponse.results)
                        trackAdapter.notifyDataSetChanged()
                        showPlaceHolder(PlaceHolderType.GOOD)
                    }
                    if (tracksResponse.results.isEmpty()) {
                        trackAdapter.notifyDataSetChanged()
                        showPlaceHolder(PlaceHolderType.NO_RESULTS)
                    }
                } else showPlaceHolder(PlaceHolderType.NO_INTERNET)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        val sharedPreferences = getSharedPreferences(PREFERENCES_TITLE, MODE_PRIVATE)
        tracksInteractor = Creator.provideTracksInteractor(sharedPreferences)
        // GoBack button
        binding.tvGoBack.setOnClickListener { finish() }
        // Clear text button
        binding.ivClear.setOnClickListener {
            showPlaceHolder(PlaceHolderType.GOOD)
            binding.etSearch.setText("")
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputManager?.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        }
        //EditText -> ActionDone
        binding.etSearch.setOnEditorActionListener { _, actionId: Int, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.etSearch.text.isNotEmpty()) search(binding.etSearch.text.toString())
            }
            false
        }
        //No internet / refresh button
        binding.btnRefresh.setOnClickListener {
            search(binding.etSearch.text.toString())
        }
        //Search history
        binding.btnClearHistory.setOnClickListener {
            tracksInteractor.clearHistory()
            binding.lrSearchHistory.visibility = View.GONE
        }
        val searchHistoryAdapter = SearchHistoryAdapter {
            tracksInteractor.saveToHistory(it)
            val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
            intent.putExtra(TRACK_EXTRA, it)
            startActivity(intent)
        }
        searchHistoryAdapter.searchHistoryTracks = tracksInteractor.loadFromHistory()
        binding.rvSearchHistory.adapter = searchHistoryAdapter
        //Search history observer
        listener = OnSharedPreferenceChangeListener { _: SharedPreferences, key: String? ->
            if (key == SEARCH_HISTORY) {
                searchHistoryAdapter.searchHistoryTracks = tracksInteractor.loadFromHistory()
                searchHistoryAdapter.notifyDataSetChanged()
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        //Recycler view search results
        trackAdapter = TrackAdapter {
            if (clickDebounce()) {
                tracksInteractor.saveToHistory(it)
                val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
                intent.putExtra(TRACK_EXTRA, it)
                startActivity(intent)
            }
        }
        trackAdapter.tracks = trackList
        binding.rvTracksSearch.adapter = trackAdapter
        //TextWatcher and setOnFocusChangeListener for etSearchText
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.ivClear.isVisible = s.isNullOrEmpty().not()
                searchText = s.toString()
                if (binding.etSearch.hasFocus() && s?.isEmpty() == true && tracksInteractor.loadFromHistory().isNotEmpty()) {
                    binding.lrSearchHistory.visibility = View.VISIBLE
                    trackList.clear()
                    trackAdapter.notifyDataSetChanged()
                } else binding.lrSearchHistory.visibility = View.GONE
                searchDebounce()
            }
        }
        binding.etSearch.addTextChangedListener(textWatcher)
        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.etSearch.text.isEmpty() && tracksInteractor.loadFromHistory().isNotEmpty()) {
                binding.lrSearchHistory.visibility = View.VISIBLE
                trackList.clear()
                trackAdapter.notifyDataSetChanged()
            } else binding.lrSearchHistory.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
        handler.removeCallbacks(clickRunnable)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        searchText = savedInstanceState?.getString(SEARCH_FIELD, SEARCH_FIELD_DEFAULT) ?: ""
        binding.etSearch.setText(searchText)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(SEARCH_FIELD, searchText)
    }

    private fun showPlaceHolder(type: PlaceHolderType) {
        when (type) {
            PlaceHolderType.GOOD -> {
                binding.lrPlaceHolder.visibility = View.GONE
            }

            PlaceHolderType.NO_INTERNET -> {
                binding.ivPlaceHolder.setImageResource(R.drawable.no_internet)
                binding.tvPlaceHolder.text = getString(R.string.no_internet)
                binding.btnRefresh.visibility = View.VISIBLE
                binding.lrPlaceHolder.visibility = View.VISIBLE
            }

            PlaceHolderType.NO_RESULTS -> {
                binding.ivPlaceHolder.setImageResource(R.drawable.empty_search_smile)
                binding.tvPlaceHolder.text = getString(R.string.no_results)
                binding.btnRefresh.visibility = View.GONE
                binding.lrPlaceHolder.visibility = View.VISIBLE
            }
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed(clickRunnable, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun search(text: String) {
        tracksInteractor.searchTracks(text, consumer)
    }

    companion object {
        const val SEARCH_FIELD = "SEARCH_FIELD"
        const val SEARCH_FIELD_DEFAULT = ""
        const val TRACK_EXTRA = "TRACK_EXTRA"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}