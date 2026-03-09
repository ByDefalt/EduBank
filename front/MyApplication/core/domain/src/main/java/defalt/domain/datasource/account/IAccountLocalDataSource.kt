package defalt.domain.datasource.account

import defalt.domain.entity.account.TokenRequest
import defalt.utils.NetworkResult

interface IAccountLocalDataSource {
    suspend fun registerToken(tokenRequest: TokenRequest): NetworkResult<Boolean>
    suspend fun unregisterToken(tokenRequest: TokenRequest): NetworkResult<Boolean>
    suspend fun getToken(): NetworkResult<TokenRequest>
}
