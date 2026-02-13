package defalt.core.api.offer.entity

import java.time.LocalDate

data class OfferEntity(
    val id: Int,
    val title: String,
    val description: String,
    val state: State,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val picturePath: String? = null,

    ) {
    enum class State(val value: String) {
        ACTIVE("active"),
        INACTIVE("inactive"),
        EXPIRED("expired"),
    }
}

