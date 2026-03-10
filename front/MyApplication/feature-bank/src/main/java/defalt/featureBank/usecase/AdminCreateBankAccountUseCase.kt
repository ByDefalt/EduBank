package defalt.featureBank.usecase

import defalt.domain.entity.bank.BankAccountCreateRequest
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.repository.service.IBankRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class AdminCreateBankAccountUseCase(
    private val bankRepository: IBankRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(accountId: String, request: BankAccountCreateRequest): NetworkResult<BankAccountDetail> {
        logger.debug("AdminCreateBankAccountUseCase")
        return bankRepository.adminCreateBankAccount(accountId, request)
    }
}
