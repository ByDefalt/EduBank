package defalt.core.api.offer.model

data class OfferEntity(
    val id: Int,
    val title: String,
    val description: String,
    val state: Offer.State,
    val startDate: java.time.LocalDate,
    val endDate: java.time.LocalDate,
    val picturePath: String? = null,

) {
    enum class State(val value: String)
}
