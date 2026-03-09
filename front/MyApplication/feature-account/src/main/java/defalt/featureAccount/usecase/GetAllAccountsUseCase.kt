package defalt.featureAccount.usecase

import defalt.domain.entity.account.Account
import defalt.domain.repository.service.IAccountRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class GetAllAccountsUseCase(
    private val repository: IAccountRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(): NetworkResult<List<Account>> {
        logger.debug("GetAllAccountsUseCase")
        return repository.getAccounts()
    }
}
