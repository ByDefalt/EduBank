package defalt.featureAccount.usecase

import defalt.domain.repository.service.IAccountRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class ActivateAccountUseCase(
    private val repository: IAccountRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: String): NetworkResult<Boolean> {
        logger.debug("ActivateAccountUseCase")
        return repository.activateAccount(id)
    }
}
