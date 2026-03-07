package defalt.featureBank.usecase

import defalt.domain.repository.service.IAccountRepository
import defalt.domain.repository.service.IOperationRepository
import defalt.utils.logger.Logger

class GetAccountDetailsAndOperation(
    private val accountRepository: IAccountRepository,
    private val operationRepository: IOperationRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke() {
        TODO()
    }
}
