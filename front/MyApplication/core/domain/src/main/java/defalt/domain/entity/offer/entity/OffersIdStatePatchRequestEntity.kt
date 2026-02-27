package defalt.core.api.offer.model

data class OffersIdStatePatchRequestEntity(
    val state: OffersIdStatePatchRequestEntity.State,

    ) {
    enum class State(val value: String)
}
