package defalt.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkResultExtensionTest {

    // ── map sur Success ────────────────────────────────────────────────────

    @Test fun `map Success transforme la valeur`() {
        val result: NetworkResult<Int> = NetworkResult.Success(5)
        val mapped = result.map { it * 2 }
        assertTrue(mapped is NetworkResult.Success)
        assertEquals(10, (mapped as NetworkResult.Success).data)
    }

    @Test fun `map Success avec transformation string to int`() {
        val result: NetworkResult<String> = NetworkResult.Success("42")
        val mapped = result.map { it.toInt() }
        assertEquals(42, (mapped as NetworkResult.Success).data)
    }

    @Test fun `map Success vers type different`() {
        val result: NetworkResult<List<Int>> = NetworkResult.Success(listOf(1, 2, 3))
        val mapped = result.map { it.size }
        assertEquals(3, (mapped as NetworkResult.Success).data)
    }

    @Test fun `map Success avec identite retourne meme valeur`() {
        val result: NetworkResult<String> = NetworkResult.Success("hello")
        val mapped = result.map { it }
        assertEquals("hello", (mapped as NetworkResult.Success).data)
    }

    // ── map sur Error ──────────────────────────────────────────────────────

    @Test fun `map Error retourne le meme Error sans appeler transform`() {
        var called = false
        val result: NetworkResult<Int> = NetworkResult.Error(404, "Not found")
        val mapped = result.map { called = true; it * 2 }
        assertTrue(mapped is NetworkResult.Error)
        assertTrue(!called)
        val error = mapped as NetworkResult.Error
        assertEquals(404, error.code)
        assertEquals("Not found", error.message)
    }

    @Test fun `map Error preserve code et message`() {
        val result: NetworkResult<String> = NetworkResult.Error(500, "Internal Server Error")
        val mapped = result.map { it.length }
        val error = mapped as NetworkResult.Error
        assertEquals(500, error.code)
        assertEquals("Internal Server Error", error.message)
    }

    @Test fun `map Error retourne meme instance`() {
        val original = NetworkResult.Error(400, "Bad Request")
        val result: NetworkResult<Int> = original
        val mapped = result.map { it + 1 }
        assertSame(original, mapped)
    }

    // ── map sur Exception ──────────────────────────────────────────────────

    @Test fun `map Exception retourne la meme Exception sans appeler transform`() {
        var called = false
        val ex = RuntimeException("crash")
        val result: NetworkResult<Int> = NetworkResult.Exception(ex)
        val mapped = result.map { called = true; it + 1 }
        assertTrue(mapped is NetworkResult.Exception)
        assertTrue(!called)
        assertSame(ex, (mapped as NetworkResult.Exception).throwable)
    }

    @Test fun `map Exception preserve le throwable`() {
        val ex = IllegalStateException("state error")
        val result: NetworkResult<String> = NetworkResult.Exception(ex)
        val mapped = result.map { it.length }
        assertSame(ex, (mapped as NetworkResult.Exception).throwable)
    }

    @Test fun `map Exception retourne meme instance`() {
        val original = NetworkResult.Exception(RuntimeException())
        val result: NetworkResult<Int> = original
        val mapped = result.map { it }
        assertSame(original, mapped)
    }

    // ── Chaining ──────────────────────────────────────────────────────────

    @Test fun `map Success deux fois enchainees`() {
        val result: NetworkResult<Int> = NetworkResult.Success(3)
        val mapped = result.map { it + 1 }.map { it * 10 }
        assertEquals(40, (mapped as NetworkResult.Success).data)
    }

    @Test fun `map Error puis map ne change pas le resultat`() {
        val result: NetworkResult<Int> = NetworkResult.Error(403, "Forbidden")
        val mapped = result.map { it + 1 }.map { it * 10 }
        assertTrue(mapped is NetworkResult.Error)
        assertEquals(403, (mapped as NetworkResult.Error).code)
    }
}
