package defalt.ui.state

import defalt.utils.NetworkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkResultMapperTest {

    @Test fun `Success se convertit en UiState Success`() {
        val result = NetworkResult.Success("hello")
        val uiState = result.toUiState { it }
        assertTrue(uiState is UiState.Success)
        assertEquals("hello", (uiState as UiState.Success).data)
    }

    @Test fun `Success avec transform se convertit correctement`() {
        val result = NetworkResult.Success(42)
        val uiState = result.toUiState { it.toString() }
        assertTrue(uiState is UiState.Success)
        assertEquals("42", (uiState as UiState.Success).data)
    }

    @Test fun `Error se convertit en UiState Error avec le message`() {
        val result = NetworkResult.Error(404, "Not found")
        val uiState = result.toUiState<String, String> { it }
        assertTrue(uiState is UiState.Error)
        assertEquals("Not found", (uiState as UiState.Error).message)
    }

    @Test fun `Exception se convertit en UiState Error`() {
        val ex = RuntimeException("crash")
        val result = NetworkResult.Exception(ex)
        val uiState = result.toUiState<String, String> { it }
        assertTrue(uiState is UiState.Error)
    }

    @Test fun `Success avec transform Unit donne UiState Success Unit`() {
        val result = NetworkResult.Success("ignored")
        val uiState = result.toUiState { }
        assertTrue(uiState is UiState.Success)
    }
}

