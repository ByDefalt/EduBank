package defalt.featureBank.usecase

import defalt.domain.repository.service.IBankRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class AdminDeleteBankAccountUseCase(
    private val repository: IBankRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: String): NetworkResult<Unit> {
        logger.debug("AdminDeleteBankAccountUseCase")
        return repository.adminDeleteBankAccount(id)
    }
}
