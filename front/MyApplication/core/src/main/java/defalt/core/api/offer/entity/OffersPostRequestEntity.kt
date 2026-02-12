package defalt.core.api.offer.model

data class OffersPostRequestEntity(
    val title: String,
    val description: String,
    val state: OffersPostRequest.State,
    val startDate: java.time.LocalDate,
    val endDate: java.time.LocalDate,
    val picturePath: String? = null,

) {
    enum class State(val value: String)
}
