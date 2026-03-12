package defalt.network.api.bank.service

import defalt.network.api.bank.model.Type
import defalt.network.infrastructure.CollectionFormats.*
import retrofit2.Response
import retrofit2.http.*

interface TypeApi {
    /**
     * GET bank/admin/types
     * [ADMIN] Liste des types
     *
     * Responses:
     *  - 200: Liste des types
     *
     * @return [kotlin.collections.List<Type>]
     */
    @GET("bank/admin/types")
    suspend fun bankAdminTypesGet(): Response<kotlin.collections.List<Type>>

    /**
     * GET bank/admin/types/{id}
     * [ADMIN] Récupérer un type
     *
     * Responses:
     *  - 200: Type trouvé
     *  - 404: Ressource non trouvée
     *
     * @param id
     * @return [Type]
     */
    @GET("bank/admin/types/{id}")
    suspend fun bankAdminTypesIdGet(@Path("id") id: kotlin.Int): Response<Type>

    /**
     * POST bank/admin/types
     * [ADMIN] Créer un type
     *
     * Responses:
     *  - 201: Type créé
     *  - 400: Requête invalide
     *
     * @param type
     * @return [Type]
     */
    @POST("bank/admin/types")
    suspend fun bankAdminTypesPost(@Body type: Type): Response<Type>
}
