package defalt.featureBank.usecase

import defalt.domain.entity.account.Account
import defalt.domain.repository.service.IAccountRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class AdminGetAllAccountsForBankUseCase(
    private val accountRepository: IAccountRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(): NetworkResult<List<Account>> {
        logger.debug("AdminGetAllAccountsForBankUseCase")
        return accountRepository.getAccounts()
    }
}
