package defalt.network.api.operation.service

import defalt.network.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import defalt.network.api.operation.model.Error
import defalt.network.api.operation.model.Operation
import defalt.network.api.operation.model.OperationList
import defalt.network.api.operation.model.OperationState

interface OperationApi {
    /**
     * GET operations/account/{accountId}
     * Récupérer les opérations d&#39;un compte
     * 
     * Responses:
     *  - 200: Liste récupérée avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @param accountId 
     * @param state  (optional)
     * @param dateFrom  (optional)
     * @param dateTo  (optional)
     * @return [OperationList]
     */
    @GET("operations/account/{accountId}")
    suspend fun operationsAccountAccountIdGet(@Path("accountId") accountId: kotlin.String, @Query("state") state: OperationState? = null, @Query("date_from") dateFrom: java.time.OffsetDateTime? = null, @Query("date_to") dateTo: java.time.OffsetDateTime? = null): Response<OperationList>

    /**
     * GET operations
     * Récupérer la liste des opérations
     * Use Case 5 (Administrateur): Voir la liste des opérations
     * Responses:
     *  - 200: Liste récupérée avec succès
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @param state  (optional)
     * @param dateFrom  (optional)
     * @param dateTo  (optional)
     * @return [OperationList]
     */
    @GET("operations")
    suspend fun operationsGet(@Query("state") state: OperationState? = null, @Query("date_from") dateFrom: java.time.OffsetDateTime? = null, @Query("date_to") dateTo: java.time.OffsetDateTime? = null): Response<OperationList>

    /**
     * POST operations/{id}/cancel
     * Créer une opération d&#39;annulation
     * Use Case 20 (Administrateur): Créer une opération d&#39;annulation
     * Responses:
     *  - 201: Opération d'annulation créée avec succès
     *  - 400: L'opération ne peut pas être annulée
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param id ID de l&#39;opération à annuler
     * @return [Operation]
     */
    @POST("operations/{id}/cancel")
    suspend fun operationsIdCancelPost(@Path("id") id: kotlin.Int): Response<Operation>

    /**
     * GET operations/{id}
     * Récupérer une opération par ID
     * Use Case 8 (Administrateur): Voir une opération spécifique avec ses détails
     * Responses:
     *  - 200: Opération récupérée avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @param id Identifiant de l&#39;opération
     * @return [Operation]
     */
    @GET("operations/{id}")
    suspend fun operationsIdGet(@Path("id") id: kotlin.Int): Response<Operation>

    /**
     * PATCH operations/{id}/state
     * Changer l&#39;état d&#39;une opération
     * Use Case 13 (Administrateur): Changer l&#39;état d&#39;une opération / Use Case 21 (Administrateur): Mettre à jour l&#39;état d&#39;une opération
     * Responses:
     *  - 200: État modifié avec succès
     *  - 404: Ressource non trouvée
     *  - 400: Requête invalide
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param id 
     * @param body 
     * @return [Operation]
     */
    @PATCH("operations/{id}/state")
    suspend fun operationsIdStatePatch(@Path("id") id: kotlin.Int, @Body body: kotlin.String): Response<Operation>

    /**
     * POST operations
     * Créer une nouvelle opération
     * Use Case 10 (Client): Effectuer un virement
     * Responses:
     *  - 201: Opération créée avec succès
     *  - 400: Requête invalide
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Solde insuffisant ou limite de découvert dépassée
     *
     * @param operation 
     * @return [Operation]
     */
    @POST("operations")
    suspend fun operationsPost(@Body operation: Operation): Response<Operation>

}
