package defalt.featureBank.usecase

import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.repository.service.IBankRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class GetAllMyAccount(
    private val bankRepository: IBankRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(): NetworkResult<List<BankAccountDetail>> {
        logger.debug("GetAllMyAccount")

        val allAccounts = bankRepository.getMyBankAccounts()
        if (allAccounts is NetworkResult.Error) return NetworkResult.Error(allAccounts.code, allAccounts.message)
        if (allAccounts is NetworkResult.Exception) return NetworkResult.Exception(allAccounts.throwable)
        allAccounts as NetworkResult.Success

        val allAccountsWithDetail = mutableListOf<BankAccountDetail>()
        for (account in allAccounts.data) {
            val accountDetail = bankRepository.getMyBankAccountById(account.id!!)
            if (accountDetail is NetworkResult.Success) {
                allAccountsWithDetail.add(accountDetail.data)
            }
        }

        return NetworkResult.Success(allAccountsWithDetail)
    }
}
