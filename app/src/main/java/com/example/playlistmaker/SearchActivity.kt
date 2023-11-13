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

class SearchActivity : AppCompatActivity() {

    private var searchText = ""
    private lateinit var etSearch: EditText

    companion object {
        const val searchField = "SEARCH_FIELD"
        const val searchFieldDefault = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        //view val
        val tvGoBack = findViewById<TextView>(R.id.tvGoBack)
        val ivClear = findViewById<ImageView>(R.id.ivClear)
        etSearch = findViewById<EditText>(R.id.etSearch)
        // GoBack button
        tvGoBack.setOnClickListener { finish() }
        // Clear text button
        ivClear.setOnClickListener{
            etSearch.setText("")
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputManager?.hideSoftInputFromWindow(etSearch.windowToken, 0)
        }
        //TextWatcher for etSearchText
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable?) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) ivClear.visibility = View.GONE else ivClear.visibility = View.VISIBLE
                searchText = s.toString()
            }
        }
        etSearch.addTextChangedListener(textWatcher)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        searchText = savedInstanceState?.getString(searchField, searchFieldDefault) ?: ""
        etSearch.setText(searchText)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(searchField, searchText)
    }

}