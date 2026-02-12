package defalt.core.api.bank.model

data class BankAccountParameterEntity(
    val id: Int,
    val overdraftLimit: Double,
    val state: BankAccountParameter.State,

) {
    enum class State(val value: String)
}
