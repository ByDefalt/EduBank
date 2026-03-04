package defalt.network.api.bank.service

import defalt.network.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


interface BankAccountPivotApi {
    /**
     * GET admin/accounts/{accountId}/bank-accounts-ids
     * [ADMIN] Liste des IDs de comptes d&#39;un utilisateur
     * Retourne les IDs de tous les comptes liés à cet utilisateur
     * Responses:
     *  - 200: Liste des IDs de comptes bancaires
     *
     * @param accountId 
     * @return [kotlin.collections.List<kotlin.String>]
     */
    @GET("admin/accounts/{accountId}/bank-accounts-ids")
    suspend fun adminAccountsAccountIdBankAccountsIdsGet(@Path("accountId") accountId: kotlin.Int): Response<kotlin.collections.List<kotlin.String>>

    /**
     * DELETE admin/accounts/{accountId}/unlink-all
     * [ADMIN] Retirer tous les comptes d&#39;un utilisateur
     * Supprime tous les liens pour cet utilisateur
     * Responses:
     *  - 204: Tous les comptes retirés
     *
     * @param accountId 
     * @return [Unit]
     */
    @DELETE("admin/accounts/{accountId}/unlink-all")
    suspend fun adminAccountsAccountIdUnlinkAllDelete(@Path("accountId") accountId: kotlin.Int): Response<Unit>

    /**
     * GET admin/bank-accounts/{bankAccountId}/accounts
     * [ADMIN] Liste des co-titulaires d&#39;un compte
     * Retourne les IDs de tous les utilisateurs liés à ce compte
     * Responses:
     *  - 200: Liste des IDs utilisateurs
     *
     * @param bankAccountId 
     * @return [kotlin.collections.List<kotlin.Int>]
     */
    @GET("admin/bank-accounts/{bankAccountId}/accounts")
    suspend fun adminBankAccountsBankAccountIdAccountsGet(@Path("bankAccountId") bankAccountId: kotlin.String): Response<kotlin.collections.List<kotlin.Int>>

    /**
     * POST admin/bank-accounts/{bankAccountId}/link/{accountId}
     * [ADMIN] Ajouter un co-titulaire
     * Lie un utilisateur à un compte bancaire
     * Responses:
     *  - 201: Co-titulaire ajouté
     *
     * @param bankAccountId 
     * @param accountId 
     * @return [Unit]
     */
    @POST("admin/bank-accounts/{bankAccountId}/link/{accountId}")
    suspend fun adminBankAccountsBankAccountIdLinkAccountIdPost(@Path("bankAccountId") bankAccountId: kotlin.String, @Path("accountId") accountId: kotlin.Int): Response<Unit>

    /**
     * DELETE admin/bank-accounts/{bankAccountId}/unlink/{accountId}
     * [ADMIN] Retirer un co-titulaire
     * Retire le lien entre un utilisateur et un compte bancaire
     * Responses:
     *  - 204: Co-titulaire retiré
     *
     * @param bankAccountId 
     * @param accountId 
     * @return [Unit]
     */
    @DELETE("admin/bank-accounts/{bankAccountId}/unlink/{accountId}")
    suspend fun adminBankAccountsBankAccountIdUnlinkAccountIdDelete(@Path("bankAccountId") bankAccountId: kotlin.String, @Path("accountId") accountId: kotlin.Int): Response<Unit>

    /**
     * DELETE admin/bank-accounts/{bankAccountId}/unlink-all
     * [ADMIN] Retirer tous les co-titulaires
     * Supprime tous les liens pour ce compte bancaire
     * Responses:
     *  - 204: Tous les co-titulaires retirés
     *
     * @param bankAccountId 
     * @return [Unit]
     */
    @DELETE("admin/bank-accounts/{bankAccountId}/unlink-all")
    suspend fun adminBankAccountsBankAccountIdUnlinkAllDelete(@Path("bankAccountId") bankAccountId: kotlin.String): Response<Unit>

}
