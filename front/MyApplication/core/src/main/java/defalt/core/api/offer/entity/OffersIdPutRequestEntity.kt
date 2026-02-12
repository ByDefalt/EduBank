package defalt.core.api.offer.model

data class OffersIdPutRequestEntity(
    val picturePath: String? = null,
    val title: String? = null,
    val description: String? = null,
    val state: OffersIdPutRequest.State? = null,
    val startDate: java.time.LocalDate? = null,
    val endDate: java.time.LocalDate? = null,

) {
    enum class State(val value: String)
}
