package defalt.utils.logger

/**
 * Logger muet (no-op) qui ignore tous les messages.
 *
 *
 * Utile pour les tests ou pour désactiver le logging sans propager des nulls.
 *
 */
class NullLogger : Logger {
    override fun trace(message: String) {
    }

    override fun debug(message: String) {
    }

    override fun info(message: String) {
    }

    override fun warn(message: String) {
    }

    override fun error(message: String) {
    }

    override fun error(message: String, t: Throwable) {
    }

    public override fun isLevelEnabled(level: LogLevel): Boolean {
        return false
    }
}
