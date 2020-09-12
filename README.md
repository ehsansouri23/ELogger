# ELogger
### Library for logging in android

### Usage
every category of logs will store in a seprate file.
to use this library you should write a class which extends `LogItem` for evety category of logs,
overriding `fileName` and `tag`, and thats done!
```
class OKHttpLogItem(
    message: String,
    override val tag: String
) : LogItem(message) {
    override val fileName: String
        get() = "OKHttpLog"
}

GlobalScope.launch {
    ELogger.d(OKHttpLogItem("log message", "TAG")
}
```