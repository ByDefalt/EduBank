package defalt.database.account

import defalt.domain.datasource.account.IAccountLocalDataSource
import defalt.domain.entity.account.TokenRequest
import defalt.utils.NetworkResult

class AccountLocalDataSource(
    private val tokenDao: TokenDao,
) : IAccountLocalDataSource {

    override suspend fun registerToken(tokenRequest: TokenRequest): NetworkResult<Boolean> =
        runCatching {
            tokenDao.insert(TokenEntity(jwt = tokenRequest.jwt))
            NetworkResult.Success(true)
        }.getOrElse { NetworkResult.Exception(it) }

    override suspend fun unregisterToken(tokenRequest: TokenRequest): NetworkResult<Boolean> =
        runCatching {
            tokenDao.delete()
            NetworkResult.Success(true)
        }.getOrElse { NetworkResult.Exception(it) }

    override suspend fun getToken(): NetworkResult<TokenRequest> =
        runCatching {
            val entity = tokenDao.get()
            if (entity != null) {
                NetworkResult.Success(TokenRequest(jwt = entity.jwt))
            } else {
                NetworkResult.Error(code = 404, message = "Aucun token enregistré")
            }
        }.getOrElse { NetworkResult.Exception(it) }
}
