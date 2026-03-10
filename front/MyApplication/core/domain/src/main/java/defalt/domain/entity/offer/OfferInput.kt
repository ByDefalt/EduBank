
package defalt.domain.entity.offer

import java.time.LocalDate


data class OfferInput (

    val title: String,

    val description: String,

    val state: OfferInput.State,

    val startDate: LocalDate,

    val endDate: LocalDate,

    val picturePath: String? = null

) {

    enum class State(val value: String) {
        ACTIVE("active"),
        INACTIVE("inactive"),
        EXPIRED("expired");
    }

}

