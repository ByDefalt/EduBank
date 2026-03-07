package defalt.network.api.bank.service

import defalt.network.api.bank.model.Type
import defalt.network.infrastructure.CollectionFormats.*
import retrofit2.Response
import retrofit2.http.*

interface TypeApi {
    /**
     * GET admin/types
     * [ADMIN] Liste des types
     *
     * Responses:
     *  - 200: Liste des types
     *
     * @return [kotlin.collections.List<Type>]
     */
    @GET("admin/types")
    suspend fun adminTypesGet(): Response<kotlin.collections.List<Type>>

    /**
     * GET admin/types/{id}
     * [ADMIN] Récupérer un type
     *
     * Responses:
     *  - 200: Type trouvé
     *  - 404: Ressource non trouvée
     *
     * @param id
     * @return [Type]
     */
    @GET("admin/types/{id}")
    suspend fun adminTypesIdGet(@Path("id") id: kotlin.Int): Response<Type>

    /**
     * POST admin/types
     * [ADMIN] Créer un type
     *
     * Responses:
     *  - 201: Type créé
     *  - 400: Requête invalide
     *
     * @param type
     * @return [Type]
     */
    @POST("admin/types")
    suspend fun adminTypesPost(@Body type: Type): Response<Type>
}
