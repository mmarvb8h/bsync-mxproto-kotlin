package bsync.myconfig


object CommandLine {

    private lateinit var cmdLine: Array<String>

    operator fun invoke(cmdLine: Array<String>) : CommandLine {
        this.cmdLine = cmdLine
        return this
    }

    val contains = fun(text: String) : Boolean {
        return cmdLine.contains(text.toLowerCase())
    }

    val find = fun(text: String) : String {
        return (cmdLine.find { it == text } ?: "").toLowerCase()
    }
}
