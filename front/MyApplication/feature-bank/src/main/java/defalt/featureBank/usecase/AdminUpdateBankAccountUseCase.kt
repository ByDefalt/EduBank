package defalt.featureBank.usecase

import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.BankAccountParameter
import defalt.domain.repository.service.IBankRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

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
