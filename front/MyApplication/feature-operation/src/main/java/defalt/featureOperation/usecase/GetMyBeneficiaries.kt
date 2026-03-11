package defalt.featureOperation.usecase

import defalt.domain.entity.operation.Beneficiary
import defalt.domain.repository.service.IOperationRepository
import defalt.domain.session.Session
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class GetMyBeneficiaries(
    private val operationRepository: IOperationRepository,
    private val session: Session,
    private val logger: Logger,
) {
    suspend operator fun invoke(): NetworkResult<List<Beneficiary>> {
        logger.debug("GetMyBeneficiaries")
        return operationRepository.getBeneficiariesByAccountId(session.accountId!!)
    }
}
