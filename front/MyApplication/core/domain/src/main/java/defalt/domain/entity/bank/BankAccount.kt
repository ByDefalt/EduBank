package defalt.domain.entity.bank

data class BankAccount(
    val id: String? = null,
    val parameterId: Int? = null,
    val typeId: Int? = null,
    val sold: Double? = null,
    val iban: String? = null,
)
