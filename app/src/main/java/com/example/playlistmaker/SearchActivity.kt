package com.example.playlistmaker

import android.content.Context
import android.hardware.input.InputManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        //view val
        val tvGoBack = findViewById<TextView>(R.id.tvGoBack)
        val etSearchText = findViewById<EditText>(R.id.etSearchText)
        val ivClear = findViewById<ImageView>(R.id.ivClear)
        // GoBack button
        tvGoBack.setOnClickListener { finish() }
        // Clear text button
        ivClear.setOnClickListener{
            etSearchText.setText("")
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputManager?.hideSoftInputFromWindow(etSearchText.windowToken, 0)
        }
        //TextWatcher for etSearchText
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable?) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) ivClear.visibility = View.GONE else ivClear.visibility = View.VISIBLE
            }
        }
        etSearchText.addTextChangedListener(textWatcher)
        //
    }
}