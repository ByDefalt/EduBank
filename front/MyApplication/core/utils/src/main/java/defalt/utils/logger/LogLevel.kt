package defalt.utils.logger

/**
 * Niveaux de journalisation disponibles. L'ordre définit la sévérité croissante
 * (TRACE le plus verbeux, ERROR le plus sévère). Les comparaisons d'ordinal
 * peuvent être utilisées pour filtrer les messages.
 */
enum class LogLevel {
    TRACE, DEBUG, INFO, WARN, ERROR
}
