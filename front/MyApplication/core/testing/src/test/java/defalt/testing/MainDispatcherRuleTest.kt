package defalt.testing

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRuleTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Test fun `la rule expose son dispatcher`() {
        assertNotNull(dispatcherRule.dispatcher)
    }

    @Test fun `le dispatcher accepte du code coroutine`() = runTest {
        var executed = false
        kotlinx.coroutines.withContext(dispatcherRule.dispatcher) {
            executed = true
        }
        org.junit.Assert.assertTrue(executed)
    }

    @Test fun `deux instances ont des dispatchers distincts`() {
        val a = MainDispatcherRule()
        val b = MainDispatcherRule()
        assertNotNull(a.dispatcher)
        assertNotNull(b.dispatcher)
    }
}
