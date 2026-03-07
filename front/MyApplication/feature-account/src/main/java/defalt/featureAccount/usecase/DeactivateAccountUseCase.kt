package defalt.featureAccount.usecase

import defalt.domain.repository.service.IAccountRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class DeactivateAccountUseCase(
    private val repository: IAccountRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: String): NetworkResult<Boolean> {
        logger.debug("DeactivateAccountUseCase")
        return repository.deactivateAccount(id)
    }
}
