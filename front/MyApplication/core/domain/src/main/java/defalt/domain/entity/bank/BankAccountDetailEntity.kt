package defalt.domain.entity.bank

/**
 * Domain entity mirroring network BankAccountDetail DTO
 */
data class BankAccountDetailEntity(
    val id: String? = null,
    val parameter: BankAccountParameterEntity? = null,
    val type: TypeEntity? = null,
    val sold: Double? = null,
    val iban: String? = null
)

