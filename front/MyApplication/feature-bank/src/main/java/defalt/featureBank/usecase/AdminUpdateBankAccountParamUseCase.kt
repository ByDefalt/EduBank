package defalt.featureBank.usecase

import defalt.domain.entity.bank.BankAccountParameter
import defalt.domain.repository.service.IBankRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class AdminUpdateBankAccountParamUseCase(
    private val repository: IBankRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: String, parameter: BankAccountParameter): NetworkResult<Unit> {
        logger.debug("AdminUpdateBankAccountParamUseCase")
        return repository.adminUpdateBankAccountParameters(id, parameter)
    }
}
