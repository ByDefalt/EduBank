package defalt.utils.logger

import java.io.IOException

/**
 * Fabrique de loggers utilitaires.
 *
 *
 * Fournit des méthodes factory pour obtenir rapidement une implémentation de
 * [Logger] adaptée (console, fichier, null). Ne garde pas d'état global.
 *
 */
object LoggerFactory {
    /**
     * Retourne un logger console configuré au niveau donné.
     *
     * @param level niveau minimal
     * @return instance de [ConsoleLogger]
     */
    fun consoleLogger(level: LogLevel): Logger {
        return ConsoleLogger(level)
    }

    /**
     * Retourne un logger muet (no-op).
     *
     * @return instance de [NullLogger]
     */
    fun nullLogger(): Logger {
        return NullLogger()
    }

    /**
     * Retourne un logger fichier configuré au niveau donné.
     *
     * @param level    niveau minimal
     * @param filePath chemin du fichier de log
     * @return instance de [FileLogger]
     * @throws IOException si le fichier ne peut pas être ouvert
     */
    @Throws(IOException::class)
    fun fileLogger(level: LogLevel, filePath: String): Logger {
        return FileLogger(level, filePath)
    }
}
