package defalt.core.api.bank.model

data class ParametersIdPutRequestEntity (
    val overdraftLimit: Double? = null,
    val state: ParametersIdPutRequest.State? = null

) {
    enum class State(val value: String) {
    }

}
