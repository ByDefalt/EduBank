package defalt.utils.logger

/**
 * Interface de logging utilisée dans l'application.
 *
 *
 * Fournit des méthodes pour journaliser des messages à différents niveaux
 * (TRACE, DEBUG, INFO, WARN, ERROR). Les implémentations peuvent router
 * les messages vers la console, un fichier, ou un sink nul.
 *
 *
 *
 * Contrat : les appels aux méthodes de logging doivent être peu coûteux pour
 * l'appelant (les implémentations peuvent filtrer par niveau avant de formater
 * le message). Les implémentations doivent être sûres en environnement
 * multi-thread si elles sont partagées entre threads.
 *
 */
interface Logger {
    /**
     * Log a trace level message (finely grained debugging information).
     *
     * @param message text to log
     */
    fun trace(message: String)

    /**
     * Log a debug level message (useful for debugging during development).
     *
     * @param message text to log
     */
    fun debug(message: String)

    /**
     * Log an informational message (normal runtime events).
     *
     * @param message text to log
     */
    fun info(message: String)

    /**
     * Log a warning message (potentially harmful situations).
     *
     * @param message text to log
     */
    fun warn(message: String)

    /**
     * Log an error message (error events that might still allow the application to continue).
     *
     * @param message text to log
     */
    fun error(message: String)

    /**
     * Log an error message with an associated Throwable (exception/stacktrace).
     *
     * @param message text to log
     * @param t       throwable to log
     */
    fun error(message: String, t: Throwable)

    /**
     * Indique si un niveau de log est activé pour cette instance.
     * Utile pour éviter de construire des messages coûteux quand le niveau est désactivé.
     *
     * @param level niveau à tester
     * @return true si le niveau est activé
     */
    fun isLevelEnabled(level: LogLevel): Boolean
}
