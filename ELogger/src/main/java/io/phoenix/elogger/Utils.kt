package io.phoenix.elogger

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun getFile(context: Context, fileName: String): File =
    File(
        File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            LOGS_DIR
        ).also {
            it.mkdirs()
        },
        fileName
    ).let { file ->
        if (!file.exists())
            file.createNewFile()
        file
    }

val dateFormatter = SimpleDateFormat("y-m-d,  HH:mm:ss", Locale.ENGLISH)
const val LOGS_DIR = "logs"

fun getNewestFile(context: Context, fileName: String): Uri? =
    context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        ?.let {
            File(it, LOGS_DIR)
                .walk()
                .sortedByDescending {
                    it.name
                        .substringAfterLast("_")
                        .substringBeforeLast(".txt")
                }.firstOrNull {
                    it.name.contains(fileName)
                }?.toUri()
        }
