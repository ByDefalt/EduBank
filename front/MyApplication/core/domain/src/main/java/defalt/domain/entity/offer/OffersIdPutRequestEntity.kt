package defalt.domain.entity.offer

import java.time.LocalDate

/**
 * Domain entity for updating an offer
 */
data class OffersIdPutRequestEntity(
    val picturePath: String? = null,
    val title: String? = null,
    val description: String? = null,
    val state: OfferState? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
)

