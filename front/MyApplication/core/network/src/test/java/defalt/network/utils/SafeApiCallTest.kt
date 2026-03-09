package defalt.network.utils

import defalt.utils.NetworkResult
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class SafeApiCallTest {

    // ── Succès ─────────────────────────────────────────────────────────────

    @Test fun `retourne Success quand body non null`() = runTest {
        val result = safeApiCall { Response.success("hello") }
        assertTrue(result is NetworkResult.Success)
        assertEquals("hello", (result as NetworkResult.Success).data)
    }

    @Test fun `retourne Success avec objet complexe`() = runTest {
        data class Dto(val id: Int, val name: String)
        val dto = Dto(1, "test")
        val result = safeApiCall { Response.success(dto) }
        assertTrue(result is NetworkResult.Success)
        assertEquals(dto, (result as NetworkResult.Success).data)
    }

    @Test fun `retourne Success avec liste`() = runTest {
        val list = listOf(1, 2, 3)
        val result = safeApiCall { Response.success(list) }
        assertTrue(result is NetworkResult.Success)
        assertEquals(3, (result as NetworkResult.Success).data.size)
    }

    // ── Body null ──────────────────────────────────────────────────────────

    @Test fun `retourne Error 200 Empty body si body null`() = runTest {
        val response = Response.success<String>(200, null)
        val result = safeApiCall { response }
        assertTrue(result is NetworkResult.Error)
        val error = result as NetworkResult.Error
        assertEquals(200, error.code)
        assertEquals("Empty body", error.message)
    }

    // ── Erreurs HTTP ───────────────────────────────────────────────────────

    @Test fun `retourne Error 404 quand reponse HTTP 404`() = runTest {
        val response = Response.error<String>(404, "not found".toResponseBody())
        val result = safeApiCall { response }
        assertTrue(result is NetworkResult.Error)
        assertEquals(404, (result as NetworkResult.Error).code)
    }

    @Test fun `retourne Error 500 quand reponse HTTP 500`() = runTest {
        val response = Response.error<String>(500, "server error".toResponseBody())
        val result = safeApiCall { response }
        assertTrue(result is NetworkResult.Error)
        assertEquals(500, (result as NetworkResult.Error).code)
    }

    @Test fun `retourne Error 401 quand reponse HTTP 401`() = runTest {
        val response = Response.error<String>(401, "unauthorized".toResponseBody())
        val result = safeApiCall { response }
        assertTrue(result is NetworkResult.Error)
        assertEquals(401, (result as NetworkResult.Error).code)
    }

    @Test fun `retourne Error 400 quand reponse HTTP 400`() = runTest {
        val response = Response.error<String>(400, "bad request".toResponseBody())
        val result = safeApiCall { response }
        assertTrue(result is NetworkResult.Error)
        assertEquals(400, (result as NetworkResult.Error).code)
    }

    // ── Exceptions ─────────────────────────────────────────────────────────

    @Test fun `retourne Exception quand appel leve une exception`() = runTest {
        val ex = RuntimeException("network crash")
        val result = safeApiCall<String> { throw ex }
        assertTrue(result is NetworkResult.Exception)
        assertSame(ex, (result as NetworkResult.Exception).throwable)
    }

    @Test fun `retourne Exception pour IOException`() = runTest {
        val ex = java.io.IOException("no connection")
        val result = safeApiCall<String> { throw ex }
        assertTrue(result is NetworkResult.Exception)
        assertSame(ex, (result as NetworkResult.Exception).throwable)
    }

    @Test fun `retourne Exception pour SocketTimeoutException`() = runTest {
        val ex = java.net.SocketTimeoutException("timeout")
        val result = safeApiCall<String> { throw ex }
        assertTrue(result is NetworkResult.Exception)
    }

    @Test fun `retourne Exception pour UnknownHostException`() = runTest {
        val ex = java.net.UnknownHostException("host")
        val result = safeApiCall<String> { throw ex }
        assertTrue(result is NetworkResult.Exception)
    }
}

