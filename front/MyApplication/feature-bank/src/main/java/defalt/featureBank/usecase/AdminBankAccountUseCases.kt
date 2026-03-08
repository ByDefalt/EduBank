package defalt.featureBank.usecase

import defalt.domain.entity.account.Account
import defalt.domain.entity.bank.BankAccountCreateRequest
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.BankAccountParameter
import defalt.domain.repository.service.IAccountRepository
import defalt.domain.repository.service.IBankRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class AdminCreateBankAccountUseCase(
    private val bankRepository: IBankRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(accountId: Int, request: BankAccountCreateRequest): NetworkResult<BankAccountDetail> {
        logger.debug("AdminCreateBankAccountUseCase")
        return bankRepository.adminCreateBankAccount(accountId, request)
    }
}

class AdminUpdateBankAccountUseCase(
    private val repository: IBankRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(
        bankAccountId: String,
        typeId: Int,
        parameter: BankAccountParameter,
    ): NetworkResult<BankAccountDetail> {
        logger.debug("AdminUpdateBankAccountUseCase")
        return repository.adminUpdateBankAccount(bankAccountId, typeId, parameter)
    }
}

class AdminGetAllAccountsForBankUseCase(
    private val accountRepository: IAccountRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(): NetworkResult<List<Account>> {
        logger.debug("AdminGetAllAccountsForBankUseCase")
        return accountRepository.getAccounts()
    }
}

