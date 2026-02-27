package defalt.core.api.bank.model

data class BankAccountsIdStatePatchRequestEntity(
    val state: BankAccountsIdStatePatchRequestEntity.State,

) {
    enum class State(val value: String)
}
