package defalt.domain.entity.offer

data class OffersIdStatePatchRequest(
    val state: OffersIdStatePatchRequest.State,
) {
    enum class State(val value: String) {
        ACTIVE("active"),
        INACTIVE("inactive"),
        EXPIRED("expired"),
        ;

        override fun toString(): String = value
    }
}
