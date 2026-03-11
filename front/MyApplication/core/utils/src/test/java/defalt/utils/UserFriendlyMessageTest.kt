package defalt.utils

import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

class UserFriendlyMessageTest {

    @Test fun `UnknownHostException retourne message connexion internet`() {
        val msg = UnknownHostException("host.example.com").toUserFriendlyMessage()
        assertTrue(msg.contains("connexion internet", ignoreCase = true))
    }

    @Test fun `ConnectException retourne message connexion serveur`() {
        val msg = ConnectException("refused").toUserFriendlyMessage()
        assertTrue(msg.contains("serveur", ignoreCase = true))
    }

    @Test fun `SocketTimeoutException retourne message timeout`() {
        val msg = SocketTimeoutException("timeout").toUserFriendlyMessage()
        assertTrue(msg.contains("temps", ignoreCase = true))
    }

    @Test fun `SSLException retourne message securite`() {
        val msg = SSLException("ssl handshake").toUserFriendlyMessage()
        assertTrue(msg.contains("curit", ignoreCase = true))
    }

    @Test fun `Exception generique retourne message inattendu`() {
        val msg = RuntimeException("unknown").toUserFriendlyMessage()
        assertTrue(msg.contains("inattendue", ignoreCase = true))
    }

    @Test fun `IllegalStateException retourne message inattendu`() {
        val msg = IllegalStateException("bad state").toUserFriendlyMessage()
        assertTrue(msg.contains("inattendue", ignoreCase = true))
    }

    @Test fun `NullPointerException retourne message inattendu`() {
        val msg = NullPointerException().toUserFriendlyMessage()
        assertTrue(msg.contains("inattendue", ignoreCase = true))
    }

    @Test fun `tous les messages ne sont pas vides`() {
        listOf(
            UnknownHostException(),
            ConnectException(),
            SocketTimeoutException(),
            SSLException("x"),
            RuntimeException(),
        ).forEach { ex ->
            assertTrue("message vide pour ${ex::class.simpleName}", ex.toUserFriendlyMessage().isNotBlank())
        }
    }
}
