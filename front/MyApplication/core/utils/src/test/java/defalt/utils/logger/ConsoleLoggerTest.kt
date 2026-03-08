package defalt.utils.logger

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ConsoleLoggerTest {

    // ── Filtrage par niveau ────────────────────────────────────────────────

    @Test fun `niveau TRACE active tous les niveaux`() {
        val logger = ConsoleLogger(LogLevel.TRACE)
        LogLevel.entries.forEach { level ->
            assertTrue("$level devrait etre actif avec TRACE", logger.isLevelEnabled(level))
        }
    }

    @Test fun `niveau ERROR desactive TRACE DEBUG INFO WARN`() {
        val logger = ConsoleLogger(LogLevel.ERROR)
        listOf(LogLevel.TRACE, LogLevel.DEBUG, LogLevel.INFO, LogLevel.WARN).forEach { level ->
            assertFalse("$level devrait etre inactif avec ERROR", logger.isLevelEnabled(level))
        }
        assertTrue(logger.isLevelEnabled(LogLevel.ERROR))
    }

    @Test fun `niveau WARN active WARN et ERROR`() {
        val logger = ConsoleLogger(LogLevel.WARN)
        assertFalse(logger.isLevelEnabled(LogLevel.TRACE))
        assertFalse(logger.isLevelEnabled(LogLevel.DEBUG))
        assertFalse(logger.isLevelEnabled(LogLevel.INFO))
        assertTrue(logger.isLevelEnabled(LogLevel.WARN))
        assertTrue(logger.isLevelEnabled(LogLevel.ERROR))
    }

    @Test fun `niveau INFO active INFO WARN ERROR`() {
        val logger = ConsoleLogger(LogLevel.INFO)
        assertFalse(logger.isLevelEnabled(LogLevel.TRACE))
        assertFalse(logger.isLevelEnabled(LogLevel.DEBUG))
        assertTrue(logger.isLevelEnabled(LogLevel.INFO))
        assertTrue(logger.isLevelEnabled(LogLevel.WARN))
        assertTrue(logger.isLevelEnabled(LogLevel.ERROR))
    }

    @Test fun `niveau DEBUG active DEBUG INFO WARN ERROR`() {
        val logger = ConsoleLogger(LogLevel.DEBUG)
        assertFalse(logger.isLevelEnabled(LogLevel.TRACE))
        assertTrue(logger.isLevelEnabled(LogLevel.DEBUG))
        assertTrue(logger.isLevelEnabled(LogLevel.INFO))
    }

    // ── Appels ne levent pas d exception ──────────────────────────────────

    @Test fun `trace ne leve pas d exception avec niveau TRACE`() {
        ConsoleLogger(LogLevel.TRACE).trace("trace msg")
    }

    @Test fun `debug ne leve pas d exception avec niveau DEBUG`() {
        ConsoleLogger(LogLevel.DEBUG).debug("debug msg")
    }

    @Test fun `info ne leve pas d exception avec niveau INFO`() {
        ConsoleLogger(LogLevel.INFO).info("info msg")
    }

    @Test fun `warn ne leve pas d exception avec niveau WARN`() {
        ConsoleLogger(LogLevel.WARN).warn("warn msg")
    }

    @Test fun `error ne leve pas d exception avec niveau ERROR`() {
        ConsoleLogger(LogLevel.ERROR).error("error msg")
    }

    @Test fun `error avec throwable ne leve pas d exception`() {
        ConsoleLogger(LogLevel.ERROR).error("error msg", RuntimeException("boom"))
    }

    @Test fun `appels filtres ne levent pas d exception non plus`() {
        val logger = ConsoleLogger(LogLevel.ERROR)
        logger.trace("filtré")
        logger.debug("filtré")
        logger.info("filtré")
        logger.warn("filtré")
    }

    @Test fun `message vide est accepte`() {
        ConsoleLogger(LogLevel.TRACE).info("")
    }

    @Test fun `message long est accepte`() {
        ConsoleLogger(LogLevel.TRACE).debug("a".repeat(10_000))
    }
}

