package defalt.domain.entity.bank

data class BankAccountCreateRequest(
    val typeId: Int,
    val iban: String,
    val sold: Double,
    val overdraftLimit: Double,
    val state: State? = null
)

