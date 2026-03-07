package defalt.featureBank.usecase

import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.repository.service.IBankRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class GetHomeAccount(
    private val repository: IBankRepository,
    private val Logger: Logger,
) {
    suspend operator fun invoke(): NetworkResult<BankAccountDetail> {
        Logger.debug("GetHomeAccount")
        return when (val listAccount = repository.getMyBankAccounts()) {
            is NetworkResult.Success -> {
                repository.getMyBankAccountById(listAccount.data.first().id!!)
            }

            is NetworkResult.Error -> {
                NetworkResult.Error(listAccount.code, listAccount.message)
            }

            is NetworkResult.Exception -> {
                NetworkResult.Exception(listAccount.throwable)
            }
        }
    }
}
