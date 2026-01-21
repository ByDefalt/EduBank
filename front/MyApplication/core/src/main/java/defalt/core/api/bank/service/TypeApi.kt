package defalt.core.api.bank.service

import defalt.core.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import defalt.core.api.bank.model.Error
import defalt.core.api.bank.model.Type
import defalt.core.api.bank.model.TypesIdPutRequest
import defalt.core.api.bank.model.TypesPostRequest

interface TypeApi {
    /**
     * GET types
     * Récupérer la liste des types de comptes
     * 
     * Responses:
     *  - 200: Liste récupérée avec succès
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @return [kotlin.collections.List<Type>]
     */
    @GET("types")
    suspend fun typesGet(): Response<kotlin.collections.List<Type>>

    /**
     * DELETE types/{id}
     * Supprimer un type de compte
     * 
     * Responses:
     *  - 204: Type supprimé avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param id 
     * @return [Unit]
     */
    @DELETE("types/{id}")
    suspend fun typesIdDelete(@Path("id") id: kotlin.Int): Response<Unit>

    /**
     * GET types/{id}
     * Récupérer un type de compte par ID
     * 
     * Responses:
     *  - 200: Type récupéré avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @param id 
     * @return [Type]
     */
    @GET("types/{id}")
    suspend fun typesIdGet(@Path("id") id: kotlin.Int): Response<Type>

    /**
     * PUT types/{id}
     * Mettre à jour un type de compte
     * 
     * Responses:
     *  - 200: Type mis à jour avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param id 
     * @param typesIdPutRequest 
     * @return [Type]
     */
    @PUT("types/{id}")
    suspend fun typesIdPut(@Path("id") id: kotlin.Int, @Body typesIdPutRequest: TypesIdPutRequest): Response<Type>

    /**
     * POST types
     * Créer un nouveau type de compte
     * Use Case 16 (Administrateur): Créer un nouveau type de compte
     * Responses:
     *  - 201: Type créé avec succès
     *  - 400: Requête invalide
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param typesPostRequest 
     * @return [Type]
     */
    @POST("types")
    suspend fun typesPost(@Body typesPostRequest: TypesPostRequest): Response<Type>

}
