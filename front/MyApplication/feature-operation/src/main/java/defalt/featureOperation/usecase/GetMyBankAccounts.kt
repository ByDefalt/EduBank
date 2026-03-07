package defalt.featureOperation.usecase

import defalt.domain.entity.bank.BankAccount
import defalt.domain.repository.service.IBankRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class GetMyBankAccounts(
    private val bankRepository: IBankRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(): NetworkResult<List<BankAccount>> {
        logger.debug("GetMyBankAccounts")
        return bankRepository.getMyBankAccounts()
    }
}
