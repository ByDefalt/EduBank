package defalt.core.api.bank.model

data class BankAccountsIdStatePatchRequestEntity (
    val state: BankAccountsIdStatePatchRequest.State

) {
    enum class State(val value: String) {
    }

}
