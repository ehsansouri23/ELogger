package io.phoenix.elogger

import android.content.Context
import android.net.Uri
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
        this@ELogger.context = context.applicationContext
        started = true
        fireDeleteOldFilesWork(context)

        GlobalScope.launch(Dispatchers.IO) {
            logChannel.consumeEach {
                try {
                    getFile(
                        context,
                        "${it.first.fileName}-${dayFormatter.format(Date())}.txt"
                    ).appendText(
                        it.first.toLogString(
                            it.second
                        )
                    )
                } catch (ignored: Exception) {

                }
            }
        }
    }

    private suspend fun log(logItem: LogItem, logLevel: LogLevel, logToFile: Boolean = true) {
        when (logLevel) {
            LogLevel.VERBOSE -> Log.v(logItem.tag, logItem.logMessage)
            LogLevel.ERROR -> Log.e(logItem.tag, logItem.logMessage)
            LogLevel.DEBUG -> Log.d(logItem.tag, logItem.logMessage)
            LogLevel.WARN -> Log.w(logItem.tag, logItem.logMessage)
            LogLevel.INFO -> Log.i(logItem.tag, logItem.logMessage)
        }
        if (started && logToFile)
            logChannel.send(logItem to logLevel)
    }

    suspend fun v(logItem: LogItem, logToFile: Boolean = true) =
        log(logItem, LogLevel.VERBOSE, logToFile)

    suspend fun d(logItem: LogItem, logToFile: Boolean = true) =
        log(logItem, LogLevel.DEBUG, logToFile)

    suspend fun i(logItem: LogItem, logToFile: Boolean = true) =
        log(logItem, LogLevel.INFO, logToFile)

    suspend fun w(logItem: LogItem, logToFile: Boolean = true) =
        log(logItem, LogLevel.WARN, logToFile)

    suspend fun e(logItem: LogItem, logToFile: Boolean = true) =
        log(logItem, LogLevel.ERROR, logToFile)

    fun getLogFile(fileName: String): Uri? =
        getNewestFile(context, fileName)
}