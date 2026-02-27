package defalt.core.api.operation.model

data class OperationsPostRequestEntity(
    val bankAccountSourceId: Int,
    val label: String,
    val ibanTarget: String,
    val amount: Double,

)
