package defalt.featureAccount.usecase

import defalt.domain.entity.account.SignInRequest
import defalt.domain.entity.account.TokenRequest
import defalt.domain.repository.service.IAccountRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class SignInClientAccountUseCase(
    private val repository: IAccountRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(email: String, password: String) : NetworkResult<TokenRequest> {
        logger.debug("SignInClientAccountUseCase")
        return repository.signIn(SignInRequest(id = email, password = password))
    }
}