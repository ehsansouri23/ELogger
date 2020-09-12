package io.phoenix.elogger

import android.content.Context
import android.os.Environment
import androidx.work.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DeleteOldLogFilesWork(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val fileExpireDate = Calendar.getInstance().also {
            it.add(Calendar.MONTH, -1)
        }
        applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.let {
            File(it, LOGS_DIR)
                .walk()
                .forEach { file ->
                    file.takeIf {
                        it.name.contains(".txt")
                    }?.takeIf {
                        it.name.contains("_")
                    }?.let { logFile ->
                        format.parse(
                            logFile.name.substringAfterLast("_").substringBeforeLast(".txt")
                        )?.let { date ->
                            Calendar.getInstance().also {
                                it.time = date
                            }.let { fileDate ->
                                if (fileDate.before(fileExpireDate))
                                    file.delete()
                            }
                        }
                    }
                }
        }
        return Result.success()
    }

    companion object {
        val TAG = "DeleteOldLogFilesWork"

        fun create(days: Long): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<DeleteOldLogFilesWork>(
                days,
                TimeUnit.DAYS
            ).run {
                setBackoffCriteria(BackoffPolicy.LINEAR, days / 2, TimeUnit.DAYS)
                addTag(TAG)
                build()
            }
        }
    }
}