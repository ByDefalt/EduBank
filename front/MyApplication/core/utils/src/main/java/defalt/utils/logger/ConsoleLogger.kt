package defalt.utils.logger

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Logger minimal qui écrit les messages vers la console (System.out / System.err)
 * selon le niveau configuré.
 *
 *
 * Le formatage inclut un timestamp et le niveau. Le logger filtre les messages
 * en comparant l'ordinal des niveaux (implémentation simple et efficace).
 *
 */
class ConsoleLogger(private val level: LogLevel) : Logger {
    private val fmt: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    private fun enabled(l: LogLevel): Boolean {
        return l.ordinal >= level.ordinal
    }

    private fun ts(): String {
        return LocalDateTime.now().format(fmt)
    }

    override fun trace(message: String) {
        if (enabled(LogLevel.TRACE)) println(format(LogLevel.TRACE, message))
    }

    override fun debug(message: String) {
        if (enabled(LogLevel.DEBUG)) println(format(LogLevel.DEBUG, message))
    }

    override fun info(message: String) {
        if (enabled(LogLevel.INFO)) println(format(LogLevel.INFO, message))
    }

    override fun warn(message: String) {
        if (enabled(LogLevel.WARN)) System.err.println(format(LogLevel.WARN, message))
    }

    override fun error(message: String) {
        if (enabled(LogLevel.ERROR)) System.err.println(format(LogLevel.ERROR, message))
    }

    override fun error(message: String, t: Throwable) {
        if (enabled(LogLevel.ERROR)) {
            System.err.println(format(LogLevel.ERROR, message))
            t.printStackTrace(System.err)
        }
    }

    private fun format(lvl: LogLevel, msg: String): String {
        return String.format("[%s] %s - %s", ts(), lvl.name, msg)
    }

    override fun isLevelEnabled(level: LogLevel): Boolean {
        return enabled(level)
    }
}
