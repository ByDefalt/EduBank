package defalt.network.api.bank.service

import defalt.network.api.bank.model.BankAccount
import defalt.network.api.bank.model.BankAccountCreateRequest
import defalt.network.api.bank.model.BankAccountDetail
import defalt.network.infrastructure.CollectionFormats.*
import retrofit2.Response
import retrofit2.http.*

interface BankAccountApi {
    /**
     * GET admin/accounts/{account_id}/bank-accounts
     * [ADMIN] Comptes d&#39;un utilisateur
     *
     * Responses:
     *  - 200: Liste des comptes
     *
     * @param accountId
     * @return [kotlin.collections.List<BankAccount>]
     */
    @GET("admin/accounts/{account_id}/bank-accounts")
    suspend fun adminAccountsAccountIdBankAccountsGet(@Path("account_id") accountId: kotlin.Int): Response<kotlin.collections.List<BankAccount>>

    /**
     * POST admin/accounts/{account_id}/bank-accounts
     * [ADMIN] Créer un compte
     *
     * Responses:
     *  - 201: Compte créé
     *  - 400: Requête invalide
     *
     * @param accountId
     * @param bankAccountCreateRequest
     * @return [BankAccountDetail]
     */
    @POST("admin/accounts/{account_id}/bank-accounts")
    suspend fun adminAccountsAccountIdBankAccountsPost(@Path("account_id") accountId: kotlin.Int, @Body bankAccountCreateRequest: BankAccountCreateRequest): Response<BankAccountDetail>

    /**
     * GET admin/bank-accounts
     * [ADMIN] Liste tous les comptes
     *
     * Responses:
     *  - 200: Liste récupérée
     *
     * @return [kotlin.collections.List<BankAccount>]
     */
    @GET("admin/bank-accounts")
    suspend fun adminBankAccountsGet(): Response<kotlin.collections.List<BankAccount>>

    /**
     * DELETE admin/bank-accounts/{id}
     * [ADMIN] Supprimer un compte
     *
     * Responses:
     *  - 204: Compte supprimé
     *  - 404: Ressource non trouvée
     *
     * @param id
     * @return [Unit]
     */
    @DELETE("admin/bank-accounts/{id}")
    suspend fun adminBankAccountsIdDelete(@Path("id") id: kotlin.String): Response<Unit>

    /**
     * GET admin/bank-accounts/{id}
     * [ADMIN] Détails d&#39;un compte
     *
     * Responses:
     *  - 200: Détails du compte
     *  - 404: Ressource non trouvée
     *
     * @param id
     * @return [BankAccountDetail]
     */
    @GET("admin/bank-accounts/{id}")
    suspend fun adminBankAccountsIdGet(@Path("id") id: kotlin.String): Response<BankAccountDetail>

    /**
     * GET my-bank-accounts
     * [CLIENT] Mes comptes actifs
     *
     * Responses:
     *  - 200: Liste de mes comptes
     *
     * @param typeId  (optional)
     * @return [kotlin.collections.List<BankAccount>]
     */
    @GET("my-bank-accounts")
    suspend fun myBankAccountsGet(@Query("type_id") typeId: kotlin.Int? = null): Response<kotlin.collections.List<BankAccount>>

    /**
     * GET my-bank-accounts/{id}/co-holders
     * [CLIENT] IDs des co-titulaires
     *
     * Responses:
     *  - 200: Liste des IDs
     *
     * @param id
     * @return [kotlin.collections.List<kotlin.Int>]
     */
    @GET("my-bank-accounts/{id}/co-holders")
    suspend fun myBankAccountsIdCoHoldersGet(@Path("id") id: kotlin.String): Response<kotlin.collections.List<kotlin.Int>>

    /**
     * GET my-bank-accounts/{id}
     * [CLIENT] Détails d&#39;un de mes comptes
     *
     * Responses:
     *  - 200: Détails du compte
     *  - 403: Accès interdit
     *  - 404: Ressource non trouvée
     *
     * @param id
     * @return [BankAccountDetail]
     */
    @GET("my-bank-accounts/{id}")
    suspend fun myBankAccountsIdGet(@Path("id") id: kotlin.String): Response<BankAccountDetail>
}
