package defalt.featureBank.usecase

import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.repository.service.IBankRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class AdminGetBankAccountByIdUseCase(
    private val repository: IBankRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: String): NetworkResult<BankAccountDetail> {
        logger.debug("AdminGetBankAccountByIdUseCase")
        return repository.adminGetBankAccountById(id)
    }
}
