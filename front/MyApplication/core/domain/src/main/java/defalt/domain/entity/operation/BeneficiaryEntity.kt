package defalt.core.api.operation.model

data class BeneficiaryEntity(
    val id: Int,
    val accountSourceId: Int,
    val ibanTarget: String,
    val name: String,

)
