package defalt.domain.entity.offer

import java.time.LocalDate

data class Offer(
    val id: Int,
    val title: String,
    val description: String,
    val state: Offer.State,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val picturePath: String? = null
) {
    enum class State(val value: String) {
        ACTIVE("active"),
        INACTIVE("inactive"),
        EXPIRED("expired");

        override fun toString(): String = value
    }
}

