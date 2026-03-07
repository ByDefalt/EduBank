package defalt.featureBank.usecase

import defalt.domain.entity.bank.BankAccount
import defalt.domain.repository.service.IBankRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class AdminGetAllBankAccountsUseCase(
    private val repository: IBankRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(): NetworkResult<List<BankAccount>> {
        logger.debug("AdminGetAllBankAccountsUseCase")
        return repository.adminGetAllBankAccounts()
    }
}
