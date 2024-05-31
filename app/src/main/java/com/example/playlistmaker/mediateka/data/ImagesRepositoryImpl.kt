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

    private val filePath =
        File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), IMAGES_DIR)

    init {
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
    }

    @SuppressLint("SimpleDateFormat")
    override suspend fun save(uri: Uri): String {
        val filename = SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(Date()) + ".jpg"
        val file = File(filePath, filename)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        //
        println("-------------")
        println("ImageRepo save from URI: [$uri]")
        println("ImageRepo save to file : [$filename]")
        //
        return filename
    }

    override suspend fun filenameToUri(filename: String): Uri? {
        val tmp = File(filePath, filename).toUri()
        println("-------------")
        println("convert from fn: [$filename]")
        if (filename.isEmpty()) println("convert to uri: [null]")
        else println("convert to uri: [$tmp]")
        return if (filename.isEmpty()) null
        else File(filePath, filename).toUri()
    }

}