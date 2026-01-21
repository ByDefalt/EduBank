package defalt.core.api.account.service

import defalt.core.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import defalt.core.api.account.model.Account
import defalt.core.api.account.model.AccountRegister

interface AccountApi {
    /**
     * GET accounts
     * Liste tous les comptes
     * 
     * Responses:
     *  - 200: Succès
     *
     * @return [kotlin.collections.List<Account>]
     */
    @GET("accounts")
    suspend fun accountsGet(): Response<kotlin.collections.List<Account>>

    /**
     * DELETE accounts/{id}
     * Supprimer un compte
     * 
     * Responses:
     *  - 204: Compte supprimé avec succès
     *
     * @param id 
     * @return [Unit]
     */
    @DELETE("accounts/{id}")
    suspend fun accountsIdDelete(@Path("id") id: kotlin.Int): Response<Unit>

    /**
     * GET accounts/{id}
     * Récupérer un compte par ID
     * 
     * Responses:
     *  - 200: Compte trouvé
     *
     * @param id 
     * @return [Account]
     */
    @GET("accounts/{id}")
    suspend fun accountsIdGet(@Path("id") id: kotlin.String): Response<Account>

    /**
     * POST accounts
     * Créer un nouveau compte
     * Nécessite un role_id et un personal_info_id existants.
     * Responses:
     *  - 201: Compte créé
     *
     * @param accountRegister 
     * @return [Account]
     */
    @POST("accounts")
    suspend fun accountsPost(@Body accountRegister: AccountRegister): Response<Account>

}
