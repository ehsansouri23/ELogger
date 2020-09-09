package io.phoenix.elogger

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Ehsan Souri, Email ehsansouri23@gmail.com, Date on 9/9/20.
 * PS: Not easy to write code, please indicate.
 */
object ELogger {
    private lateinit var context: Context
    private var started = false
    private val logChannel = Channel<Pair<LogItem, LogLevel>>()

    private val dayFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)


    fun start(context: Context) {
        this@ELogger.context = context
        started = true

        GlobalScope.launch(Dispatchers.IO) {
            logChannel.consumeEach {
                getFile(
                    context,
                    "${it.first.fileName}-${dayFormatter.format(Date())}.txt"
                ).appendText(
                    it.first.toLogString(
                        it.second
                    )
                )
            }
        }
    }

    suspend fun log(logItem: LogItem, logLevel: LogLevel) {
        when (logLevel) {
            LogLevel.VERBOSE -> Log.v(logItem.tag, logItem.logMessage)
            LogLevel.ERROR -> Log.e(logItem.tag, logItem.logMessage)
            LogLevel.DEBUG -> Log.d(logItem.tag, logItem.logMessage)
            LogLevel.WARN -> Log.w(logItem.tag, logItem.logMessage)
            LogLevel.INFO -> Log.i(logItem.tag, logItem.logMessage)
        }
        if (started)
            logChannel.send(logItem to logLevel)
    }

    suspend fun v(logItem: LogItem) =
        log(logItem, LogLevel.VERBOSE)

    suspend fun d(logItem: LogItem) =
        log(logItem, LogLevel.DEBUG)

    suspend fun i(logItem: LogItem) =
        log(logItem, LogLevel.INFO)

    suspend fun w(logItem: LogItem) =
        log(logItem, LogLevel.WARN)

    suspend fun e(logItem: LogItem) =
        log(logItem, LogLevel.ERROR)

    fun deleteOldLogFiles() {

    }
}