package defalt.domain.entity.bank

data class BankAccountParameter(
    val id: Int? = null,
    val overdraftLimit: Double? = null,
    val state: State? = null,
)
