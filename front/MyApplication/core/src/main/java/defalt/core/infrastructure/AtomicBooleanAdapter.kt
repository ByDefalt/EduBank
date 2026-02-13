package defalt.core.infrastructure

import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object AtomicBooleanAdapter : KSerializer<AtomicBoolean> {
    override fun serialize(encoder: Encoder, value: AtomicBoolean) {
        encoder.encodeBoolean(value.get())
    }

    override fun deserialize(decoder: Decoder): AtomicBoolean = AtomicBoolean(decoder.decodeBoolean())

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("AtomicBoolean", PrimitiveKind.BOOLEAN)
}
