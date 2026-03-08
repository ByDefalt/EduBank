package defalt.utils.logger

import org.junit.Assert.assertFalse
import org.junit.Test

class NullLoggerTest {

    private val logger = NullLogger()

    @Test fun `trace ne lance pas d exception`() { logger.trace("test trace") }
    @Test fun `debug ne lance pas d exception`() { logger.debug("test debug") }
    @Test fun `info ne lance pas d exception`() { logger.info("test info") }
    @Test fun `warn ne lance pas d exception`() { logger.warn("test warn") }
    @Test fun `error message ne lance pas d exception`() { logger.error("test error") }

    @Test fun `error message et throwable ne lance pas d exception`() {
        logger.error("test error", RuntimeException("boom"))
    }

    @Test fun `isLevelEnabled retourne false pour tous les niveaux`() {
        LogLevel.entries.forEach { level ->
            assertFalse("isLevelEnabled devrait etre false pour $level", logger.isLevelEnabled(level))
        }
    }

    @Test fun `appels multiples sans erreur`() {
        repeat(100) { logger.debug("msg $it") }
    }
}
