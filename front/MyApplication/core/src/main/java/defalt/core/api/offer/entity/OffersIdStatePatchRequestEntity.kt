package defalt.core.api.offer.model

data class OffersIdStatePatchRequestEntity(
    val state: OffersIdStatePatchRequest.State,

) {
    enum class State(val value: String)
}
