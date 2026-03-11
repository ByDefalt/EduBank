package defalt.network.serialization

import defalt.network.api.operation.model.Operation
import defalt.network.api.operation.model.OperationState
import defalt.network.infrastructure.Serializer
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.OffsetDateTime

class OperationSerializationTest {

    @Test
    fun `serialize and deserialize operation retains state`() {
        val json = Serializer.kotlinxSerializationJson

        val op = Operation(
            id = 42,
            accountSourceId = "1",
            label = "Test",
            state = OperationState.COMPLETED,
            ibanTarget = "FR7630006000019876543210987",
            amount = 100.0,
            date = OffsetDateTime.parse("2026-01-15T14:30:00Z"),
        )

        val serialized = json.encodeToString(Operation.serializer(), op)
        val deserialized = json.decodeFromString(Operation.serializer(), serialized)

        assertEquals(OperationState.COMPLETED, deserialized.state)
    }
}
