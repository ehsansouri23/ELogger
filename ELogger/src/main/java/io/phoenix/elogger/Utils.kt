package io.phoenix.elogger

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun getFile(context: Context, fileName: String): File =
    File(
        context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
        fileName
    ).let { file ->
        if (!file.exists())
            file.createNewFile()
        file
    }

val dateFormatter = SimpleDateFormat("MMMM d,  HH-mm-ss", Locale.ENGLISH)