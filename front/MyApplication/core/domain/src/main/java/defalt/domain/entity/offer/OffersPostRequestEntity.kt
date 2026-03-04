package defalt.domain.entity.offer

import java.time.LocalDate

/**
 * Domain entity for creating an offer
 */
data class OffersPostRequestEntity(
    val title: String,
    val description: String,
    val state: OfferState,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val picturePath: String? = null
)

