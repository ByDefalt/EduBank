package defalt.core.api.bank.model

data class ParametersPostRequestEntity (
    val overdraftLimit: Double,
    val state: ParametersPostRequest.State

) {
    enum class State(val value: String) {
    }

}
