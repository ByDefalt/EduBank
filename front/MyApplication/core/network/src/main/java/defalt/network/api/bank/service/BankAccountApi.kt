package defalt.network.api.bank.service

import defalt.network.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import defalt.network.api.bank.model.BankAccountCreateRequest
import defalt.network.api.bank.model.BankAccountDetail
import defalt.network.api.bank.model.Error

interface BankAccountApi {
    /**
     * GET admin/accounts/{account_id}/bank-accounts
     * [ADMIN] Comptes bancaires d&#39;un utilisateur
     * 
     * Responses:
     *  - 200: Liste des comptes
     *
     * @param accountId 
     * @return [kotlin.collections.List<BankAccountDetail>]
     */
    @GET("admin/accounts/{account_id}/bank-accounts")
    suspend fun adminAccountsAccountIdBankAccountsGet(@Path("account_id") accountId: kotlin.Int): Response<kotlin.collections.List<BankAccountDetail>>

    /**
     * POST admin/accounts/{account_id}/bank-accounts
     * [ADMIN] Créer un compte bancaire pour un utilisateur
     * Crée un compte bancaire complet en une seule opération: - Crée les paramètres du compte (découvert, état) - Crée le compte bancaire avec un type existant - Lie automatiquement le compte à l&#39;utilisateur 
     * Responses:
     *  - 201: Compte créé avec succès
     *  - 400: Données invalides
     *
     * @param accountId 
     * @param bankAccountCreateRequest 
     * @return [BankAccountDetail]
     */
    @POST("admin/accounts/{account_id}/bank-accounts")
    suspend fun adminAccountsAccountIdBankAccountsPost(@Path("account_id") accountId: kotlin.Int, @Body bankAccountCreateRequest: BankAccountCreateRequest): Response<BankAccountDetail>

    /**
     * GET admin/bank-accounts
     * [ADMIN] Récupérer TOUS les comptes bancaires
     * Liste complète de tous les comptes
     * Responses:
     *  - 200: Liste récupérée
     *
     * @return [kotlin.collections.List<BankAccountDetail>]
     */
    @GET("admin/bank-accounts")
    suspend fun adminBankAccountsGet(): Response<kotlin.collections.List<BankAccountDetail>>

    /**
     * DELETE admin/bank-accounts/{id}
     * [ADMIN] Supprimer un compte bancaire
     * Supprime le compte, ses liens (co-titulaires) ET ses paramètres
     * Responses:
     *  - 204: Compte supprimé avec succès
     *  - 404: Compte non trouvé
     *
     * @param id 
     * @return [Unit]
     */
    @DELETE("admin/bank-accounts/{id}")
    suspend fun adminBankAccountsIdDelete(@Path("id") id: kotlin.String): Response<Unit>

    /**
     * GET admin/bank-accounts/{id}
     * [ADMIN] Détails d&#39;un compte bancaire
     * 
     * Responses:
     *  - 200: Détails du compte
     *  - 404: Compte non trouvé
     *
     * @param id 
     * @return [BankAccountDetail]
     */
    @GET("admin/bank-accounts/{id}")
    suspend fun adminBankAccountsIdGet(@Path("id") id: kotlin.String): Response<BankAccountDetail>

    /**
     * GET my-bank-accounts
     * [CLIENT] Mes comptes bancaires actifs
     * Un client ne voit QUE ses propres comptes avec état &#39;active&#39;
     * Responses:
     *  - 200: Liste de mes comptes
     *
     * @param typeId Filtrer par type de compte (optional)
     * @return [kotlin.collections.List<BankAccountDetail>]
     */
    @GET("my-bank-accounts")
    suspend fun myBankAccountsGet(@Query("type_id") typeId: kotlin.Int? = null): Response<kotlin.collections.List<BankAccountDetail>>

    /**
     * GET my-bank-accounts/{id}/co-holders
     * [CLIENT] IDs des co-titulaires
     * Retourne uniquement les IDs des autres utilisateurs qui partagent ce compte. Les noms et infos détaillées sont disponibles via Account-API. 
     * Responses:
     *  - 200: Liste des IDs
     *  - 403: Ce compte ne vous appartient pas
     *  - 404: Compte non trouvé
     *
     * @param id 
     * @return [kotlin.collections.List<kotlin.Int>]
     */
    @GET("my-bank-accounts/{id}/co-holders")
    suspend fun myBankAccountsIdCoHoldersGet(@Path("id") id: kotlin.String): Response<kotlin.collections.List<kotlin.Int>>

    /**
     * GET my-bank-accounts/{id}
     * [CLIENT] Détails complets d&#39;un de mes comptes
     * Retourne TOUTES les infos du compte: - Solde actuel - Découvert autorisé - État du compte - Type de compte - IBAN  Le client ne peut accéder qu&#39;à SES propres comptes. 
     * Responses:
     *  - 200: Détails du compte
     *  - 403: Ce compte ne vous appartient pas
     *  - 404: Compte non trouvé
     *
     * @param id 
     * @return [BankAccountDetail]
     */
    @GET("my-bank-accounts/{id}")
    suspend fun myBankAccountsIdGet(@Path("id") id: kotlin.String): Response<BankAccountDetail>

}
