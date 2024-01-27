package com.example.playlistmaker.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker.PREFERENCES_TITLE
import com.example.playlistmaker.R
import com.example.playlistmaker.SEARCH_HISTORY
import com.example.playlistmaker.adapters.SearchHistoryAdapter
import com.example.playlistmaker.adapters.TrackAdapter
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.iTunes.ITunesApi
import com.example.playlistmaker.iTunes.PlaceHolderType
import com.example.playlistmaker.iTunes.TracksResponse
import com.example.playlistmaker.utils.SearchHistory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private var searchText = ""
    //for search results
    private val trackList = ArrayList<Track>()
    private lateinit var trackAdapter: TrackAdapter
    //for search history
    private lateinit var listener: OnSharedPreferenceChangeListener
    //retrofit
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        binding.etSearch.setOnEditorActionListener{ _, actionId: Int, _ ->
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
        val sharedPreferences = getSharedPreferences(PREFERENCES_TITLE, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPreferences)
        binding.btnClearHistory.setOnClickListener {
            searchHistory.clearHistory()
            binding.lrSearchHistory.visibility = View.GONE
        }
        val searchHistoryAdapter = SearchHistoryAdapter {
            searchHistory.saveToHistory(it)
            val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
            intent.putExtra(trackExtra, it)
            startActivity(intent)
        }
        searchHistoryAdapter.searchHistoryTracks = searchHistory.loadHistory()
        binding.rvSearchHistory.adapter = searchHistoryAdapter
        //Search history observer
        listener = OnSharedPreferenceChangeListener { _: SharedPreferences, key: String? ->
            if (key == SEARCH_HISTORY) {
                searchHistoryAdapter.searchHistoryTracks = searchHistory.loadHistory()
                searchHistoryAdapter.notifyDataSetChanged()
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        //Recycler view search results
        trackAdapter = TrackAdapter {
            searchHistory.saveToHistory(it)
            val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
            intent.putExtra(trackExtra, it)
            startActivity(intent)
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
                if (binding.etSearch.hasFocus() && s?.isEmpty() == true && searchHistory.loadHistory().isNotEmpty()) {
                    binding.lrSearchHistory.visibility = View.VISIBLE
                    trackList.clear()
                    trackAdapter.notifyDataSetChanged()
                }
                else binding.lrSearchHistory.visibility = View.GONE
            }
        }
        binding.etSearch.addTextChangedListener(textWatcher)
        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.etSearch.text.isEmpty() && searchHistory.loadHistory().isNotEmpty()) {
                binding.lrSearchHistory.visibility = View.VISIBLE
                trackList.clear()
                trackAdapter.notifyDataSetChanged()
            }
            else binding.lrSearchHistory.visibility = View.GONE
        }
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        searchText = savedInstanceState?.getString(searchField, searchFieldDefault) ?: ""
        binding.etSearch.setText(searchText)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(searchField, searchText)
    }

    private fun search(text: String) {
        iTunesService.findTrack(text).enqueue(object : Callback<TracksResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                if (response.code() == 200) {
                    trackList.clear()
                    val results = response.body()?.results
                    if (results?.isNotEmpty() == true) {
                        trackList.addAll(results)
                        trackAdapter.notifyDataSetChanged()
                        showPlaceHolder(PlaceHolderType.GOOD)
                    }
                    if (trackList.isEmpty()) {
                        trackAdapter.notifyDataSetChanged()
                        showPlaceHolder(PlaceHolderType.NO_RESULTS)
                    }
                }
                else {
                    showPlaceHolder(PlaceHolderType.NO_INTERNET)
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                trackList.clear()
                trackAdapter.notifyDataSetChanged()
                showPlaceHolder(PlaceHolderType.NO_INTERNET)
                Log.d(getString(R.string.app_name), "Failure: ${t.message}")
            }

        })
    }

    private fun showPlaceHolder(type: PlaceHolderType) {
        when(type) {
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

    companion object {
        const val searchField = "SEARCH_FIELD"
        const val searchFieldDefault = ""
        const val trackExtra = "TRACK_EXTRA"
    }

}