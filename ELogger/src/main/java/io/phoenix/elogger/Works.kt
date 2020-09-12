package io.phoenix.elogger

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager

fun fireDeleteOldFilesWork(context: Context) =
    WorkManager.getInstance(context)
        .enqueueUniquePeriodicWork(
            DeleteOldLogFilesWork.TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            DeleteOldLogFilesWork.create(1)
        )