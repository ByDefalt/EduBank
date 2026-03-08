package defalt.utils.logger

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LoggerFactoryTest {

    @Test fun `consoleLogger retourne une instance non nulle`() {
        val logger = LoggerFactory.consoleLogger(LogLevel.DEBUG)
        assertNotNull(logger)
        assertTrue(logger is ConsoleLogger)
    }

    @Test fun `consoleLogger avec niveau TRACE`() {
        val logger = LoggerFactory.consoleLogger(LogLevel.TRACE)
        assertTrue(logger.isLevelEnabled(LogLevel.TRACE))
    }

    @Test fun `consoleLogger avec niveau ERROR`() {
        val logger = LoggerFactory.consoleLogger(LogLevel.ERROR)
        assertTrue(logger.isLevelEnabled(LogLevel.ERROR))
    }

    @Test fun `nullLogger retourne une instance non nulle`() {
        val logger = LoggerFactory.nullLogger()
        assertNotNull(logger)
        assertTrue(logger is NullLogger)
    }

    @Test fun `nullLogger desactive tous les niveaux`() {
        val logger = LoggerFactory.nullLogger()
        LogLevel.entries.forEach { level ->
            assertTrue(!logger.isLevelEnabled(level))
        }
    }

    @Test fun `deux appels consoleLogger retournent instances distinctes`() {
        val a = LoggerFactory.consoleLogger(LogLevel.DEBUG)
        val b = LoggerFactory.consoleLogger(LogLevel.DEBUG)
        assertTrue(a !== b)
    }

    @Test fun `deux appels nullLogger retournent instances distinctes`() {
        val a = LoggerFactory.nullLogger()
        val b = LoggerFactory.nullLogger()
        assertTrue(a !== b)
    }
}

