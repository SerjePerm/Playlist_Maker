package com.example.playlistmaker.mediateka.data

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.mediateka.domain.ImagesRepository
import com.example.playlistmaker.utils.Constants.Companion.IMAGES_DIR
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class ImagesRepositoryImpl(private val context: Context) : ImagesRepository {

    @SuppressLint("SimpleDateFormat")
    override suspend fun save(uri: Uri): String {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), IMAGES_DIR)
        if (!filePath.exists()) { filePath.mkdirs() }
        val filename = SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(Date()) + ".jpg"
        val file = File(filePath, filename)
        try{
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        } catch (e: Exception) {
            println("ImagesRepository saving file: ${file.toURI()} error: ${e.message}")
        }
        return filename
    }

    override suspend fun filenameToUri(filename: String): Uri? {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), IMAGES_DIR)
        return if (filename.isEmpty()) null
        else File(filePath, filename).toUri()
    }

}