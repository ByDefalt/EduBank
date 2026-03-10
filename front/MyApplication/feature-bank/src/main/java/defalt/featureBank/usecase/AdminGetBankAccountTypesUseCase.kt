package defalt.featureBank.usecase

import defalt.domain.entity.bank.Type
import defalt.domain.repository.service.IBankRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class AdminGetBankAccountTypesUseCase(
    private val repository: IBankRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(): NetworkResult<List<Type>> {
        logger.debug("AdminGetBankAccountTypesUseCase")
        return repository.adminGetAllTypes()
    }
}
