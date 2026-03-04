package defalt.domain.entity.bank

data class BankAccountDetail(
    val id: String? = null,
    val parameter: BankAccountParameter? = null,
    val type: Type? = null,
    val sold: Double? = null,
    val iban: String? = null
)

