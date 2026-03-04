package defalt.domain.entity.operation

data class Beneficiary(
    val accountSourceId: String,
    val ibanTarget: String,
    val name: String,
    val id: Int? = null
)

