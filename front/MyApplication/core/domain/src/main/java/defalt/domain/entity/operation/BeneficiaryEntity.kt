package defalt.domain.entity.operation

/**
 * Domain entity mirroring network Beneficiary DTO
 */
data class BeneficiaryEntity(
    val accountSourceId: String,
    val ibanTarget: String,
    val name: String,
    val id: Int? = null
)

