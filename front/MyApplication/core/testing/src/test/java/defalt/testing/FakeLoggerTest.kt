package defalt.testing

import defalt.utils.logger.LogLevel
import org.junit.Assert.assertFalse
import org.junit.Test

class FakeLoggerTest {

    private val logger = FakeLogger()

    @Test fun `trace ne leve pas d exception`() { logger.trace("msg") }

    @Test fun `debug ne leve pas d exception`() { logger.debug("msg") }

    @Test fun `info ne leve pas d exception`() { logger.info("msg") }

    @Test fun `warn ne leve pas d exception`() { logger.warn("msg") }

    @Test fun `error message ne leve pas d exception`() { logger.error("msg") }

    @Test fun `error message et throwable ne leve pas d exception`() {
        logger.error("msg", RuntimeException("boom"))
    }

    @Test fun `isLevelEnabled retourne false pour tous les niveaux`() {
        LogLevel.entries.forEach { level ->
            assertFalse("devrait etre false pour $level", logger.isLevelEnabled(level))
        }
    }

    @Test fun `appels multiples sans erreur`() {
        repeat(50) {
            logger.trace("t$it")
            logger.debug("d$it")
            logger.info("i$it")
            logger.warn("w$it")
            logger.error("e$it")
        }
    }

    @Test fun `message vide accepte`() {
        logger.debug("")
        logger.info("")
    }
}
