package defalt.core.api.offer.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

data class OfferEntity(
    val id: Int,
    val title: String,
    val description: String,
    val state: OfferEntity.State,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val picturePath: String? = null,

    ) {
    enum class State(val value: kotlin.String) {
        ACTIVE("active"),
        INACTIVE("inactive"),
        EXPIRED("expired"),
    }
}

