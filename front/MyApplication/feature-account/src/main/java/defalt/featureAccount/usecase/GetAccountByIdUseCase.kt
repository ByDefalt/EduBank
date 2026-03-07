package defalt.featureAccount.usecase

import defalt.domain.entity.account.Account
import defalt.domain.entity.account.PersonalInformation
import defalt.domain.repository.service.IAccountRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

data class AccountWithInfo(val account: Account, val personalInfo: PersonalInformation)

class GetAccountByIdUseCase(
    private val repository: IAccountRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: String): NetworkResult<AccountWithInfo> {
        logger.debug("GetAccountByIdUseCase")
        val accountResult = repository.getAccountById(id)
        if (accountResult is NetworkResult.Error) return NetworkResult.Error(accountResult.code, accountResult.message)
        if (accountResult is NetworkResult.Exception) return NetworkResult.Exception(accountResult.throwable)
        accountResult as NetworkResult.Success

        val infoResult = repository.getPersonalInformationByAccountId(id)
        if (infoResult is NetworkResult.Error) return NetworkResult.Error(infoResult.code, infoResult.message)
        if (infoResult is NetworkResult.Exception) return NetworkResult.Exception(infoResult.throwable)
        infoResult as NetworkResult.Success

        return NetworkResult.Success(AccountWithInfo(accountResult.data, infoResult.data))
    }
}
