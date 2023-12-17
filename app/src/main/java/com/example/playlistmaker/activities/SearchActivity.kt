package com.example.playlistmaker.activities

import android.annotation.SuppressLint
import android.content.Context
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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.PREFERENCES_TITLE
import com.example.playlistmaker.R
import com.example.playlistmaker.SEARCH_HISTORY
import com.example.playlistmaker.adapters.SearchHistoryAdapter
import com.example.playlistmaker.adapters.TrackAdapter
import com.example.playlistmaker.data.Track
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

    private var searchText = ""
    private lateinit var etSearch: EditText
    //for search results
    private val trackList = ArrayList<Track>()
    private lateinit var trackAdapter: TrackAdapter
    //for search history
    private val searchHistoryAdapter = SearchHistoryAdapter()
    private lateinit var listener: OnSharedPreferenceChangeListener
    //placeholder
    private lateinit var lrPlaceHolder: LinearLayout
    private lateinit var ivPlaceHolder: ImageView
    private lateinit var tvPlaceHolder: TextView
    private lateinit var btnRefresh: TextView
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
        setContentView(R.layout.activity_search)
        //view val
        val rvTracksSearch = findViewById<RecyclerView>(R.id.rvTracksSearch)
        val rvSearchHistory = findViewById<RecyclerView>(R.id.rvSearchHistory)
        val tvGoBack = findViewById<TextView>(R.id.tvGoBack)
        val ivClear = findViewById<ImageView>(R.id.ivClear)
        val btnClearHistory = findViewById<Button>(R.id.btnClearHistory)
        val lrSearchHistory = findViewById<LinearLayout>(R.id.lrSearchHistory)
        etSearch = findViewById(R.id.etSearch)
        //placeholder val
        lrPlaceHolder = findViewById(R.id.lrPlaceHolder)
        ivPlaceHolder = findViewById(R.id.ivPlaceHolder)
        tvPlaceHolder = findViewById(R.id.tvPlaceHolder)
        btnRefresh = findViewById(R.id.btnRefresh)
        // GoBack button
        tvGoBack.setOnClickListener { finish() }
        // Clear text button
        ivClear.setOnClickListener {
            showPlaceHolder(PlaceHolderType.GOOD)
            etSearch.setText("")
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputManager?.hideSoftInputFromWindow(etSearch.windowToken, 0)
        }
        //EditText -> ActionDone
        etSearch.setOnEditorActionListener{ _, actionId: Int, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (etSearch.text.isNotEmpty()) search(etSearch.text.toString())
            }
            false
        }
        //No internet / refresh button
        btnRefresh.setOnClickListener {
            search(etSearch.text.toString())
        }
        //Search history
        val sharedPreferences = getSharedPreferences(PREFERENCES_TITLE, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPreferences)
        btnClearHistory.setOnClickListener {
            searchHistory.clearHistory()
            lrSearchHistory.visibility = View.GONE
        }
        searchHistoryAdapter.searchHistoryTracks = searchHistory.loadHistory()
        rvSearchHistory.adapter = searchHistoryAdapter
        //Search history observer
        listener = OnSharedPreferenceChangeListener { _: SharedPreferences, key: String? ->
            if (key == SEARCH_HISTORY) {
                searchHistoryAdapter.searchHistoryTracks = searchHistory.loadHistory()
                searchHistoryAdapter.notifyDataSetChanged()
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        //Recycler view search results
        trackAdapter = TrackAdapter { searchHistory.saveToHistory(it) }
        trackAdapter.tracks = trackList
        rvTracksSearch.adapter = trackAdapter
        //TextWatcher and setOnFocusChangeListener for etSearchText
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                ivClear.isVisible = s.isNullOrEmpty().not()
                searchText = s.toString()
                if (etSearch.hasFocus() && s?.isEmpty() == true && searchHistory.loadHistory().isNotEmpty()) {
                    lrSearchHistory.visibility = View.VISIBLE
                    trackList.clear()
                    trackAdapter.notifyDataSetChanged()
                }
                else lrSearchHistory.visibility = View.GONE
            }
        }
        etSearch.addTextChangedListener(textWatcher)
        etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && etSearch.text.isEmpty() && searchHistory.loadHistory().isNotEmpty()) {
                lrSearchHistory.visibility = View.VISIBLE
                trackList.clear()
                trackAdapter.notifyDataSetChanged()
            }
            else lrSearchHistory.visibility = View.GONE
        }
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        searchText = savedInstanceState?.getString(searchField, searchFieldDefault) ?: ""
        etSearch.setText(searchText)
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
                lrPlaceHolder.visibility = View.GONE
            }
            PlaceHolderType.NO_INTERNET -> {
                ivPlaceHolder.setImageResource(R.drawable.no_internet)
                tvPlaceHolder.text = getString(R.string.no_internet)
                btnRefresh.visibility = View.VISIBLE
                lrPlaceHolder.visibility = View.VISIBLE
            }
            PlaceHolderType.NO_RESULTS -> {
                ivPlaceHolder.setImageResource(R.drawable.empty_search_smile)
                tvPlaceHolder.text = getString(R.string.no_results)
                btnRefresh.visibility = View.GONE
                lrPlaceHolder.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val searchField = "SEARCH_FIELD"
        const val searchFieldDefault = ""
    }

}