package com.example.playlistmaker.utils

import android.content.Context
import android.util.TypedValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Locale

fun dpToPx(dp: Int, context: Context): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
}

fun msToTime(ms: Int): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(ms)

fun String.toIntList(): ArrayList<Int> {
    val type = object : TypeToken<ArrayList<Int>>() {}.type
    val result: ArrayList<Int> = Gson().fromJson(this, type)
    return result
}