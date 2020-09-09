package io.phoenix.elogger

import java.util.*

abstract class LogItem(
    val logMessage: String,
) {

    abstract val fileName: String

    abstract val tag: String

    fun toLogString(logLevel: LogLevel): String =
        logLevel.let { "${dateFormatter.format(Date())}/ $it/$tag: $logMessage\n" }

}