package defalt.featureOperation.usecase

import defalt.domain.entity.operation.Beneficiary
import defalt.domain.repository.service.IOperationRepository
import defalt.domain.session.Session
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class AddBeneficiary(
    private val operationRepository: IOperationRepository,
    private val session: Session,
    private val logger: Logger,
) {
    suspend operator fun invoke(name: String, iban: String): NetworkResult<Beneficiary> {
        logger.debug("AddBeneficiary")
        val beneficiary = Beneficiary(
            accountSourceId = session.accountId!!,
            ibanTarget = iban,
            name = name,
        )
        return operationRepository.createBeneficiary(beneficiary)
    }
}
