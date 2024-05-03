package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.models.PlaceHolderType
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.adapters.SearchHistoryAdapter
import com.example.playlistmaker.search.ui.adapters.SearchTracksAdapter
import com.example.playlistmaker.utils.Constants.Companion.TRACK_EXTRA
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModel()

    //for search results
    private val resultsTrackList = ArrayList<Track>()
    private lateinit var searchTracksAdapter: SearchTracksAdapter

    //for search history
    private val historyTrackList = ArrayList<Track>()
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDebounce()
        initializeAdapters()
        initializeListeners()
        initializeObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeDebounce() {
        onTrackClickDebounce = debounce(
            delayMillis = CLICK_DEBOUNCE_DELAY_MILLIS,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = false
        ) { track ->
            viewModel.saveToHistory(track)
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            intent.putExtra(TRACK_EXTRA, track)
            startActivity(intent)
        }

    }

    private fun initializeAdapters() {
        //Recycler view search results
        searchTracksAdapter = SearchTracksAdapter {
            onTrackClickDebounce(it)
        }
        searchTracksAdapter.tracks = resultsTrackList
        binding.rvTracksSearch.adapter = searchTracksAdapter
        //Recycler view search history
        searchHistoryAdapter = SearchHistoryAdapter {
            onTrackClickDebounce(it)
        }
        searchHistoryAdapter.searchHistoryTracks = historyTrackList
        binding.rvSearchHistory.adapter = searchHistoryAdapter
    }

    private fun initializeListeners() {
        //Clear search history button
        binding.btnClearHistory.setOnClickListener { viewModel.clearHistoryClick() }
        //No internet / refresh button
        binding.btnRefresh.setOnClickListener {
            viewModel.searchRequest(binding.etSearch.text.toString())
        }
        //Clear text button
        binding.ivClear.setOnClickListener {
            hideKeyboard()
            binding.etSearch.setText("")
            viewModel.loadAndSetSearchHistory()
            binding.lrSearchHistory.isVisible = true
        }
        //Set focus listener for search edit text
        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            binding.lrSearchHistory.isVisible =
                hasFocus && binding.etSearch.text.isEmpty() && historyTrackList.isNotEmpty()
        }
        //Text watcher for search edit text
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.ivClear.isVisible = s.isNullOrEmpty().not()
                viewModel.searchDebounce(s.toString())
                if (binding.etSearch.hasFocus() && s?.isEmpty() == true) {
                    viewModel.loadAndSetSearchHistory()
                    binding.lrSearchHistory.isVisible = true
                } else {
                    binding.lrSearchHistory.isVisible = false
                    binding.rvTracksSearch.isVisible = true
                }
            }
        }
        binding.etSearch.addTextChangedListener(textWatcher)
    }

    private fun initializeObservers() {
        //Screen state observer
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is SearchScreenState.Error -> {
                    showErrorScreen()
                }

                SearchScreenState.Loading -> {
                    showLoadingScreen()
                }

                is SearchScreenState.SearchHistory -> {
                    setSearchHistory(screenState.historyList)
                }

                is SearchScreenState.SearchResults -> {
                    showSearchResults(screenState.resultsList)
                }
            }
        }
    }

    private fun showErrorScreen() {
        showPlaceHolder(PlaceHolderType.NO_INTERNET)
        //Hide others
        binding.progressBar.isVisible = false
        binding.rvTracksSearch.isVisible = false
        binding.lrSearchHistory.isVisible = false
    }

    private fun showLoadingScreen() {
        binding.progressBar.isVisible = true
        //Hide others
        hideKeyboard()
        binding.rvTracksSearch.isVisible = false
        binding.lrSearchHistory.isVisible = false
        showPlaceHolder(PlaceHolderType.GOOD)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setSearchHistory(historyList: List<Track>) {
        if (historyList.isNotEmpty()) {
            historyTrackList.clear()
            historyTrackList.addAll(historyList)
            searchHistoryAdapter.notifyDataSetChanged()
        }
        //Hide all
        binding.lrSearchHistory.isVisible = false
        binding.progressBar.isVisible = false
        binding.rvTracksSearch.isVisible = false
        showPlaceHolder(PlaceHolderType.GOOD)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchResults(resultsList: List<Track>) {
        if (resultsList.isEmpty()) {
            showPlaceHolder(PlaceHolderType.NO_RESULTS)
            binding.rvTracksSearch.isVisible = false
        }
        if (resultsList.isNotEmpty()) {
            showPlaceHolder(PlaceHolderType.GOOD)
            resultsTrackList.clear()
            resultsTrackList.addAll(resultsList)
            searchTracksAdapter.notifyDataSetChanged()
            binding.rvTracksSearch.isVisible = true
        }
        //Hide others
        binding.progressBar.isVisible = false
        binding.lrSearchHistory.isVisible = false
    }

    private fun showPlaceHolder(type: PlaceHolderType) {
        when (type) {
            PlaceHolderType.GOOD -> {
                binding.lrPlaceHolder.isVisible = false
            }

            PlaceHolderType.NO_INTERNET -> {
                binding.ivPlaceHolder.setImageResource(R.drawable.no_internet)
                binding.tvPlaceHolder.text = getString(R.string.no_internet)
                binding.btnRefresh.isVisible = true
                binding.lrPlaceHolder.isVisible = true
            }

            PlaceHolderType.NO_RESULTS -> {
                binding.ivPlaceHolder.setImageResource(R.drawable.empty_search_smile)
                binding.tvPlaceHolder.text = getString(R.string.no_results)
                binding.btnRefresh.isVisible = false
                binding.lrPlaceHolder.isVisible = true
            }
        }
    }

    private fun hideKeyboard() {
        val inputManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputManager?.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }

}