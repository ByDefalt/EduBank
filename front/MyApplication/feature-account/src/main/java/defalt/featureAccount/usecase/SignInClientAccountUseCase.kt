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

        if(signInResult is NetworkResult.Success) {
            val validateResult = repository.validateToken(signInResult.data)
            if(validateResult is NetworkResult.Success){
                // gérer le cas où le rôle renvoyé n'est pas un enum connu
                val role = try {
                    RoleEnum.valueOf(validateResult.data.role!!)
                } catch (e: Exception) {
                    RoleEnum.CUSTOMER
                }
                session.accountId = validateResult.data.id
                session.role = role
                session.token = signInResult.data.jwt
                return NetworkResult.Success(session.role!!)
            }
        }
        return NetworkResult.Error(401, "Unauthorized")
    }
}
