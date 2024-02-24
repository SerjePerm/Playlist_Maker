package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.models.PlaceHolderType
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.adapters.SearchHistoryAdapter
import com.example.playlistmaker.search.ui.adapters.SearchTracksAdapter
import com.example.playlistmaker.utils.Constants.Companion.TRACK_EXTRA

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    //for search results
    private val resultsTrackList = ArrayList<Track>()
    private lateinit var searchTracksAdapter: SearchTracksAdapter
    //for search history
    private val historyTrackList = ArrayList<Track>()
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    /*
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var binding: ActivitySearchBinding
    private var searchText = ""
    private lateinit var viewModel: SearchViewModel

    //for search results
    private val trackList = ArrayList<Track>()
    private lateinit var trackAdapter: TrackAdapter
    private var isClickAllowed = true
    //private val searchRunnable = Runnable { search(binding.etSearch.text.toString()) }
    private val clickRunnable = Runnable { isClickAllowed = true }

    //for search history
    private lateinit var listener: OnSharedPreferenceChangeListener

    // TracksInteractor + consumer
    private lateinit var tracksInteractor: TracksInteractor


    @SuppressLint("NotifyDataSetChanged")
    private val consumer = Consumer<TracksResponse> { tracksResponse ->
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
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]
        initializeAdapters()
        initializeListeners()
        initializeObservers()
    }

    private fun initializeAdapters() {
        //Recycler view search results
        searchTracksAdapter = SearchTracksAdapter {
            viewModel.saveToHistory(it)
            val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
            intent.putExtra(TRACK_EXTRA, it)
            startActivity(intent)
        }
        searchTracksAdapter.tracks = resultsTrackList
        binding.rvTracksSearch.adapter = searchTracksAdapter
        //Recycler view search history
        searchHistoryAdapter = SearchHistoryAdapter {
            viewModel.saveToHistory(it)
            val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
            intent.putExtra(TRACK_EXTRA, it)
            startActivity(intent)
        }
        searchHistoryAdapter.searchHistoryTracks = historyTrackList
        binding.rvSearchHistory.adapter = searchHistoryAdapter
    }

    private fun initializeListeners() {
        //GoBack button
        binding.tvGoBack.setOnClickListener { finish() }
        //Clear text button
        binding.ivClear.setOnClickListener { viewModel.clearSearchTextClick() }
        //Clear search history button
        binding.btnClearHistory.setOnClickListener { viewModel.clearHistoryClick() }
        //No internet / refresh button
        binding.btnRefresh.setOnClickListener {
            viewModel.searchDebounce(searchText = binding.etSearch.text.toString())
        }
        //Text watcher for search edit text
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearchTextChanged(s.toString())
            }
        }
        binding.etSearch.addTextChangedListener(textWatcher)
        //Set focus listener for search edit text
        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onSearchTextFocusChanged(
                searchText = binding.etSearch.text.toString(),
                hasFocus = hasFocus
            )
        }
    }

    private fun initializeObservers() {
        //Screen state observer
        viewModel.screenState.observe(this@SearchActivity) { screenState ->
            when (screenState) {
                is SearchScreenState.Error -> { println("Error") }
                SearchScreenState.Loading -> { println("Loading") }
                is SearchScreenState.SearchHistory -> { showSearchHistory(screenState.historyList) }
                is SearchScreenState.SearchResults -> { showSearchResults(screenState.resultsList) }
                SearchScreenState.ClearSearchHistory -> { println("ClearSearchHistory") }
                SearchScreenState.EmptyResults -> { println("EmptyResults") }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchHistory(historyList: List<Track>) {
        //clear and hide search results
        resultsTrackList.clear()
        searchTracksAdapter.notifyDataSetChanged()
        //set and show search history
        historyTrackList.clear()
        historyTrackList.addAll(historyList)
        searchHistoryAdapter.notifyDataSetChanged()
        binding.lrSearchHistory.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchResults(resultsList: List<Track>) {
        //binding.progressBar.visibility = View.GONE
        //binding.rvTracksSearch.visibility = View.VISIBLE
        resultsTrackList.clear()
        resultsTrackList.addAll(resultsList)
        searchTracksAdapter.notifyDataSetChanged()
        showPlaceHolder(PlaceHolderType.GOOD)
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

    /*
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
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
        viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory())[SearchViewModel::class.java]
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed(clickRunnable, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val SEARCH_FIELD = "SEARCH_FIELD"
        const val SEARCH_FIELD_DEFAULT = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
     */

}