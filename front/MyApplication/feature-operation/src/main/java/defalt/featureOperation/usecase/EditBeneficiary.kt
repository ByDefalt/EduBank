package defalt.featureOperation.usecase

import defalt.domain.entity.operation.Beneficiary
import defalt.domain.repository.service.IOperationRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class EditBeneficiary(
    private val operationRepository: IOperationRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: Int, name: String, iban: String, accountSourceId: String): NetworkResult<Beneficiary> {
        logger.debug("EditBeneficiary")
        val beneficiary = Beneficiary(
            accountSourceId = accountSourceId,
            ibanTarget = iban,
            name = name,
            id = id,
        )
        return operationRepository.updateBeneficiary(id, beneficiary)
    }
}
