package defalt.featureAccount.usecase

import defalt.domain.entity.account.RoleEnum
import defalt.domain.repository.service.IAccountRepository
import defalt.domain.session.Session
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class TakeLocalTokenUseCase(
    private val repository: IAccountRepository,
    private val session: Session,
    private val logger: Logger,
) {
    suspend operator fun invoke(): NetworkResult<RoleEnum> {
        val tokenResponse = repository.getSavedToken()
        if(tokenResponse is NetworkResult.Success){
            val validateResult = repository.validateToken(tokenResponse.data)
            if(validateResult is NetworkResult.Success){
                session.accountId = validateResult.data.id
                session.role = RoleEnum.valueOf(validateResult.data.role!!)
                session.token = tokenResponse.data.jwt
                logger.debug("id : ${session.accountId}")
                logger.debug("role : ${session.role}")
                logger.debug("token : ${session.token}")
                repository.insertTokenInHeaders(tokenResponse.data.jwt)
                return NetworkResult.Success(session.role!!)
            }
        }
        return NetworkResult.Error(401, "Unauthorized")
    }
}