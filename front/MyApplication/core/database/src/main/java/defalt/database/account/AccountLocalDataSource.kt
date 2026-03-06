package defalt.database.account

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import defalt.domain.datasource.account.IAccountLocalDataSource
import defalt.domain.entity.account.TokenRequest
import defalt.utils.NetworkResult
import kotlinx.coroutines.flow.firstOrNull

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class AccountLocalDataSource(
    private val context: Context,
) : IAccountLocalDataSource {

    private companion object {
        val KEY_JWT = stringPreferencesKey("jwt")
    }

    override suspend fun registerToken(tokenRequest: TokenRequest): NetworkResult<Boolean> =
        runCatching {
            context.dataStore.edit { prefs ->
                prefs[KEY_JWT] = tokenRequest.jwt
            }
            NetworkResult.Success(true)
        }.getOrElse { NetworkResult.Exception(it) }

    override suspend fun unregisterToken(tokenRequest: TokenRequest): NetworkResult<Boolean> =
        runCatching {
            context.dataStore.edit { it.clear() }
            NetworkResult.Success(true)
        }.getOrElse { NetworkResult.Exception(it) }

    override suspend fun getToken(): NetworkResult<TokenRequest> =
        runCatching {
            val prefs = context.dataStore.data.firstOrNull()
            val jwt = prefs?.get(KEY_JWT)
            if (jwt != null) {
                NetworkResult.Success(TokenRequest(jwt = jwt))
            } else {
                NetworkResult.Error(code = 404, message = "Aucun token enregistré")
            }
        }.getOrElse { NetworkResult.Exception(it) }
}
