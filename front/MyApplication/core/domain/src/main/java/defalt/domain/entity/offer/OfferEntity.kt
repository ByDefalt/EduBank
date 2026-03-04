package defalt.domain.entity.offer

import java.time.LocalDate

/**
 * Domain entity mirroring network Offer DTO
 */
data class OfferEntity(
    val id: Int,
    val title: String,
    val description: String,
    val state: OfferState,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val picturePath: String? = null
)

enum class OfferState(val value: String) {
    ACTIVE("active"),
    INACTIVE("inactive"),
    EXPIRED("expired")
}

