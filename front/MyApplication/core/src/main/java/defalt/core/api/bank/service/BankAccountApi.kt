package defalt.core.api.bank.service

import defalt.core.api.bank.model.BankAccount
import defalt.core.api.bank.model.BankAccountDetails
import defalt.core.api.bank.model.BankAccountsIdBalanceGet200Response
import defalt.core.api.bank.model.BankAccountsIdPutRequest
import defalt.core.api.bank.model.BankAccountsIdStatePatchRequest
import defalt.core.api.bank.model.BankAccountsPostRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface BankAccountApi {
    /**
     * GET bank-accounts/current
     * Afficher le compte courant de l&#39;utilisateur connecté
     * Use Case 3 (Client): Afficher le compte courant
     * Responses:
     *  - 200: Compte courant récupéré avec succès
     *  - 404: Aucun compte courant trouvé
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @return [BankAccountDetails]
     */
    @GET("bank-accounts/current")
    suspend fun bankAccountsCurrentGet(): Response<BankAccountDetails>

    /**
     * GET bank-accounts
     * Récupérer la liste des comptes bancaires
     * Use Case 5 (Client): Voir la liste des comptes / Use Case 3 (Administrateur): Voir la liste des comptes bancaires
     * Responses:
     *  - 200: Liste récupérée avec succès
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @param accountId Filtrer par ID de compte utilisateur (optional)
     * @param typeId Filtrer par type de compte (optional)
     * @param state Filtrer par état (via paramètres) (optional)
     * @return [kotlin.collections.List<BankAccountDetails>]
     */
    @GET("bank-accounts")
    suspend fun bankAccountsGet(@Query("account_id") accountId: kotlin.Int? = null, @Query("type_id") typeId: kotlin.Int? = null, @Query("state") state: kotlin.String? = null): Response<kotlin.collections.List<BankAccountDetails>>

    /**
     * GET bank-accounts/{id}/balance
     * Récupérer le solde d&#39;un compte bancaire
     *
     * Responses:
     *  - 200: Solde récupéré avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @param id
     * @return [BankAccountsIdBalanceGet200Response]
     */
    @GET("bank-accounts/{id}/balance")
    suspend fun bankAccountsIdBalanceGet(@Path("id") id: kotlin.Int): Response<BankAccountsIdBalanceGet200Response>

    /**
     * DELETE bank-accounts/{id}
     * Supprimer un compte bancaire
     *
     * Responses:
     *  - 204: Compte bancaire supprimé avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param id
     * @return [Unit]
     */
    @DELETE("bank-accounts/{id}")
    suspend fun bankAccountsIdDelete(@Path("id") id: kotlin.Int): Response<Unit>

    /**
     * GET bank-accounts/{id}
     * Récupérer un compte bancaire par ID
     * Use Case 4 (Client): Voir les détails d&#39;un compte / Use Case 7 (Administrateur): Voir un compte bancaire spécifique avec ses détails
     * Responses:
     *  - 200: Compte bancaire récupéré avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param id Numéro du compte bancaire
     * @return [BankAccountDetails]
     */
    @GET("bank-accounts/{id}")
    suspend fun bankAccountsIdGet(@Path("id") id: kotlin.Int): Response<BankAccountDetails>

    /**
     * PUT bank-accounts/{id}
     * Mettre à jour un compte bancaire
     *
     * Responses:
     *  - 200: Compte bancaire mis à jour avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param id
     * @param bankAccountsIdPutRequest
     * @return [BankAccount]
     */
    @PUT("bank-accounts/{id}")
    suspend fun bankAccountsIdPut(@Path("id") id: kotlin.Int, @Body bankAccountsIdPutRequest: BankAccountsIdPutRequest): Response<BankAccount>

    /**
     * PATCH bank-accounts/{id}/state
     * Changer l&#39;état d&#39;un compte bancaire
     * Use Case 11 (Administrateur): Changer l&#39;état d&#39;un compte bancaire
     * Responses:
     *  - 200: État modifié avec succès
     *  - 404: Ressource non trouvée
     *  - 400: Requête invalide
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param id
     * @param bankAccountsIdStatePatchRequest
     * @return [BankAccount]
     */
    @PATCH("bank-accounts/{id}/state")
    suspend fun bankAccountsIdStatePatch(@Path("id") id: kotlin.Int, @Body bankAccountsIdStatePatchRequest: BankAccountsIdStatePatchRequest): Response<BankAccount>

    /**
     * POST bank-accounts
     * Créer un nouveau compte bancaire
     *
     * Responses:
     *  - 201: Compte bancaire créé avec succès
     *  - 400: Requête invalide
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param bankAccountsPostRequest
     * @return [BankAccount]
     */
    @POST("bank-accounts")
    suspend fun bankAccountsPost(@Body bankAccountsPostRequest: BankAccountsPostRequest): Response<BankAccount>
}
