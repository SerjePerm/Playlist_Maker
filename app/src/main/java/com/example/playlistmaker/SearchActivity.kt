package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.adapters.TrackAdapter
import com.example.playlistmaker.data.mock_data

class SearchActivity : AppCompatActivity() {

    private var searchText = ""
    private lateinit var etSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        //view val
        val rvTracksSearch = findViewById<RecyclerView>(R.id.rvTracksSearch)
        val tvGoBack = findViewById<TextView>(R.id.tvGoBack)
        val ivClear = findViewById<ImageView>(R.id.ivClear)
        etSearch = findViewById<EditText>(R.id.etSearch)
        // GoBack button
        tvGoBack.setOnClickListener { finish() }
        // Clear text button
        ivClear.setOnClickListener {
            etSearch.setText("")
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
        //Recycler view
        val trackAdapter = TrackAdapter(mock_data)
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

    companion object {
        const val searchField = "SEARCH_FIELD"
        const val searchFieldDefault = ""
    }

}