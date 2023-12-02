package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.adapters.TrackAdapter
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.iTunes.ITunesApi
import com.example.playlistmaker.iTunes.PlaceHolderType
import com.example.playlistmaker.iTunes.TracksResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var searchText = ""
    private lateinit var etSearch: EditText
    private var trackList = ArrayList<Track>()
    private val trackAdapter = TrackAdapter()
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        //view val
        val rvTracksSearch = findViewById<RecyclerView>(R.id.rvTracksSearch)
        val tvGoBack = findViewById<TextView>(R.id.tvGoBack)
        val ivClear = findViewById<ImageView>(R.id.ivClear)
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
        //TextWatcher for etSearchText
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                ivClear.isVisible = s.isNullOrEmpty().not()
                searchText = s.toString()
            }
        }
        etSearch.addTextChangedListener(textWatcher)
        //EditText -> ActionDone
        etSearch.setOnEditorActionListener{ _, actionId: Int, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (etSearch.text.isNotEmpty()) search()
            }
            false
        }
        //No internet / refresh button
        btnRefresh.setOnClickListener {
            search()
        }
        //Recycler view
        trackAdapter.tracks = trackList
        rvTracksSearch.adapter = trackAdapter
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

    private fun search() {
        iTunesService.findTrack(etSearch.text.toString()).enqueue(object : Callback<TracksResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                if (response.code() == 200) {
                    trackList.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        trackList.addAll(response.body()?.results!!)
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