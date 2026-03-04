package defalt.domain.entity.offer

import java.time.LocalDate

data class OffersIdPutRequest(
    val picturePath: String? = null,
    val title: String? = null,
    val description: String? = null,
    val state: OffersIdPutRequest.State? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
) {
    enum class State(val value: String) {
        ACTIVE("active"),
        INACTIVE("inactive"),
        EXPIRED("expired");

        override fun toString(): String = value
    }
}

