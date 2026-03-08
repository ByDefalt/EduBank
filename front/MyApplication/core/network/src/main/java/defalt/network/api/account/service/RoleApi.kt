package defalt.network.api.account.service

import defalt.network.api.account.model.Role
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RoleApi {
    /**
     * GET roles
     * Liste tous les rôles (Admin uniquement)
     *
     * Responses:
     *  - 200: Liste récupérée avec succès
     *  - 401: Non autorisé (Token manquant ou invalide)
     *  - 403: Interdit (Permission insuffisante)
     *
     * @return [kotlin.collections.List<Role>]
     */
    @GET("roles")
    suspend fun rolesGet(): Response<kotlin.collections.List<Role>>

    /**
     * GET roles/{id}
     * Récupérer un rôle par son ID
     *
     * Responses:
     *  - 200: Rôle trouvé
     *  - 404: Rôle non trouvé
     *
     * @param id
     * @return [Role]
     */
    @GET("roles/{id}")
    suspend fun rolesIdGet(@Path("id") id: kotlin.Int): Response<Role>

    /**
     * GET roles/name/{name}
     * Récupérer le rôle associé à un compte
     *
     * Responses:
     *  - 200: Rôle trouvé
     *  - 404: Compte ou Rôle non trouvé
     *
     * @param name role du compte
     * @return [Role]
     */
    @GET("roles/name/{name}")
    suspend fun rolesNameNameGet(@Path("name") name: kotlin.String): Response<Role>
}
