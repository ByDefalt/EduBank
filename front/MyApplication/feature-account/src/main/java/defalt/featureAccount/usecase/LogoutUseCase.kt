package defalt.featureAccount.usecase

import defalt.domain.repository.service.IAccountRepository
import defalt.domain.session.Session
import defalt.utils.NetworkResult

class LogoutUseCase(
    private val repository: IAccountRepository,
    private val session: Session,
) {
    suspend operator fun invoke(): NetworkResult<Boolean> {
        session.token = null
        session.accountId = null
        session.role = null
        repository.signOut()
        return NetworkResult.Success(true)
    }
}
