package defalt.core.api.offer.service

import defalt.core.api.offer.model.Offer
import defalt.core.api.offer.model.OffersIdPutRequest
import defalt.core.api.offer.model.OffersIdStatePatchRequest
import defalt.core.api.offer.model.OffersPostRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OfferApi {
    /**
     * GET offers/active
     * Récupérer uniquement les offres actives
     * Offres disponibles pour les visiteurs et clients
     * Responses:
     *  - 200: Liste des offres actives
     *
     * @return [kotlin.collections.List<Offer>]
     */
    @GET("offers/active")
    suspend fun offersActiveGet(): Response<kotlin.collections.List<Offer>>

    /**
     * enum for parameter state
     */
    @Serializable
    enum class StateOffersGet(val value: kotlin.String) {
        @SerialName(value = "active")
        ACTIVE("active"),

        @SerialName(value = "inactive")
        INACTIVE("inactive"),

        @SerialName(value = "expired")
        EXPIRED("expired"),
    }

    /**
     * GET offers
     * Récupérer la liste des offres
     * Use Case: Visualiser les offres (Visiteur/Client), Voir la liste des offres (Administrateur)
     * Responses:
     *  - 200: Liste récupérée avec succès
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @param state Filtrer par état (optional)
     * @param activeOnly Afficher uniquement les offres actives (pour visiteur/client) (optional, default to true)
     * @return [kotlin.collections.List<Offer>]
     */
    @GET("offers")
    suspend fun offersGet(@Query("state") state: StateOffersGet? = null, @Query("active_only") activeOnly: kotlin.Boolean? = true): Response<kotlin.collections.List<Offer>>

    /**
     * DELETE offers/{id}
     * Supprimer une offre
     * Use Case 19: Supprimer une offre (Administrateur)
     * Responses:
     *  - 204: Offre supprimée avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param id
     * @return [Unit]
     */
    @DELETE("offers/{id}")
    suspend fun offersIdDelete(@Path("id") id: kotlin.Int): Response<Unit>

    /**
     * GET offers/{id}
     * Récupérer une offre par ID
     * Use Case 9: Voir une offre spécifique avec ses détails (Administrateur)
     * Responses:
     *  - 200: Offre récupérée avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @param id Identifiant de l&#39;offre
     * @return [Offer]
     */
    @GET("offers/{id}")
    suspend fun offersIdGet(@Path("id") id: kotlin.Int): Response<Offer>

    /**
     * PUT offers/{id}
     * Mettre à jour une offre
     * Use Case 18: Mettre à jour une offre (Administrateur)
     * Responses:
     *  - 200: Offre mise à jour avec succès
     *  - 404: Ressource non trouvée
     *  - 400: Requête invalide
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param id
     * @param offersIdPutRequest
     * @return [Offer]
     */
    @PUT("offers/{id}")
    suspend fun offersIdPut(@Path("id") id: kotlin.Int, @Body offersIdPutRequest: OffersIdPutRequest): Response<Offer>

    /**
     * PATCH offers/{id}/state
     * Changer l&#39;état d&#39;une offre
     * Use Case 12: Changer l&#39;état d&#39;une offre (Administrateur)
     * Responses:
     *  - 200: État modifié avec succès
     *  - 404: Ressource non trouvée
     *  - 400: Requête invalide
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param id
     * @param offersIdStatePatchRequest
     * @return [Offer]
     */
    @PATCH("offers/{id}/state")
    suspend fun offersIdStatePatch(@Path("id") id: kotlin.Int, @Body offersIdStatePatchRequest: OffersIdStatePatchRequest): Response<Offer>

    /**
     * POST offers
     * Créer une nouvelle offre
     * Use Case 17: Créer une offre (Administrateur)
     * Responses:
     *  - 201: Offre créée avec succès
     *  - 400: Requête invalide
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *  - 403: Accès interdit - Permissions insuffisantes
     *
     * @param offersPostRequest
     * @return [Offer]
     */
    @POST("offers")
    suspend fun offersPost(@Body offersPostRequest: OffersPostRequest): Response<Offer>
}
