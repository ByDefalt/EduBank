package defalt.featureAccount.usecase

import defalt.domain.session.Session

class LogoutUseCase(
    private val session: Session,
) {
    operator fun invoke() {
        session.token = null
        session.accountId = null
        session.role = null
    }
}
