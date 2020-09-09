package io.phoenix.elogger

import java.util.*

abstract class LogItem(
    val logMessage: String,
    val logLevel: LogLevel
) {

    abstract val fileName: String

    abstract val tag: String

    override fun toString(): String =
        logLevel.let { "${dateFormatter.format(Date())}/ $it/$tag: $logMessage\n" }

}