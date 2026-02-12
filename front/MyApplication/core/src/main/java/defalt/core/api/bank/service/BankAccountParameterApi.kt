package defalt.core.api.bank.service

import defalt.core.api.bank.model.BankAccountParameter
import defalt.core.api.bank.model.ParametersIdPutRequest
import defalt.core.api.bank.model.ParametersPostRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BankAccountParameterApi {
    /**
     * GET parameters
     * Récupérer la liste des paramètres de comptes bancaires
     *
     * Responses:
     *  - 200: Liste récupérée avec succès
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @return [kotlin.collections.List<BankAccountParameter>]
     */
    @GET("parameters")
    suspend fun parametersGet(): Response<kotlin.collections.List<BankAccountParameter>>

    /**
     * DELETE parameters/{id}
     * Supprimer des paramètres de compte bancaire
     *
     * Responses:
     *  - 204: Paramètres supprimés avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param id
     * @return [Unit]
     */
    @DELETE("parameters/{id}")
    suspend fun parametersIdDelete(@Path("id") id: kotlin.Int): Response<Unit>

    /**
     * GET parameters/{id}
     * Récupérer des paramètres par ID
     *
     * Responses:
     *  - 200: Paramètres récupérés avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @param id
     * @return [BankAccountParameter]
     */
    @GET("parameters/{id}")
    suspend fun parametersIdGet(@Path("id") id: kotlin.Int): Response<BankAccountParameter>

    /**
     * PUT parameters/{id}
     * Mettre à jour des paramètres de compte bancaire
     * Use Case 15 (Administrateur): Mettre à jour les paramètres d&#39;un compte bancaire
     * Responses:
     *  - 200: Paramètres mis à jour avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param id
     * @param parametersIdPutRequest
     * @return [BankAccountParameter]
     */
    @PUT("parameters/{id}")
    suspend fun parametersIdPut(@Path("id") id: kotlin.Int, @Body parametersIdPutRequest: ParametersIdPutRequest): Response<BankAccountParameter>

    /**
     * POST parameters
     * Créer de nouveaux paramètres de compte bancaire
     *
     * Responses:
     *  - 201: Paramètres créés avec succès
     *  - 400: Requête invalide
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param parametersPostRequest
     * @return [BankAccountParameter]
     */
    @POST("parameters")
    suspend fun parametersPost(@Body parametersPostRequest: ParametersPostRequest): Response<BankAccountParameter>
}
