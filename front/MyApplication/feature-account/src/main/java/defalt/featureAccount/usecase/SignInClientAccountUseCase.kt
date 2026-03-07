package defalt.featureAccount.usecase

import defalt.domain.entity.account.RoleEnum
import defalt.domain.entity.account.SignInRequest
import defalt.domain.repository.service.IAccountRepository
import defalt.domain.session.Session
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class SignInClientAccountUseCase(
    private val repository: IAccountRepository,
    private val session: Session,
    private val logger: Logger,
) {
    suspend operator fun invoke(email: String, password: String): NetworkResult<RoleEnum> {
        logger.debug("SignInClientAccountUseCase")

        val signInResult = repository.signIn(SignInRequest(id = email, password = password))
        if (signInResult is NetworkResult.Error) return NetworkResult.Error(signInResult.code, signInResult.message)
        if (signInResult is NetworkResult.Exception) return NetworkResult.Exception(signInResult.throwable)

        // Le rôle est stocké en session par AccountRemoteDataSource.signIn via validateToken
        val role = when (session.role?.uppercase()) {
            RoleEnum.ADMIN.value -> RoleEnum.ADMIN
            else -> RoleEnum.CUSTOMER
        }

        return NetworkResult.Success(role)
    }
}
