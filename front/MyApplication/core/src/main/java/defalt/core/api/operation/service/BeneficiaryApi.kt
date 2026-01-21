package defalt.core.api.operation.service

import defalt.core.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import defalt.core.api.operation.model.BeneficiariesIdPutRequest
import defalt.core.api.operation.model.BeneficiariesPostRequest
import defalt.core.api.operation.model.Beneficiary
import defalt.core.api.operation.model.Error

interface BeneficiaryApi {
    /**
     * GET beneficiaries
     * Récupérer la liste des bénéficiaires
     * Use Case 8 (Client): Voir la liste des bénéficiaires
     * Responses:
     *  - 200: Liste récupérée avec succès
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @param accountSourceId Filtrer par compte source (optional)
     * @return [kotlin.collections.List<Beneficiary>]
     */
    @GET("beneficiaries")
    suspend fun beneficiariesGet(@Query("account_source_id") accountSourceId: kotlin.Int? = null): Response<kotlin.collections.List<Beneficiary>>

    /**
     * DELETE beneficiaries/{id}
     * Supprimer un bénéficiaire
     * Use Case 9 (Client): Supprimer un bénéficiaire
     * Responses:
     *  - 204: Bénéficiaire supprimé avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @param id 
     * @return [Unit]
     */
    @DELETE("beneficiaries/{id}")
    suspend fun beneficiariesIdDelete(@Path("id") id: kotlin.Int): Response<Unit>

    /**
     * GET beneficiaries/{id}
     * Récupérer un bénéficiaire par ID
     * 
     * Responses:
     *  - 200: Bénéficiaire récupéré avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @param id 
     * @return [Beneficiary]
     */
    @GET("beneficiaries/{id}")
    suspend fun beneficiariesIdGet(@Path("id") id: kotlin.Int): Response<Beneficiary>

    /**
     * PUT beneficiaries/{id}
     * Mettre à jour un bénéficiaire
     * Use Case 7 (Client): Modifier un bénéficiaire
     * Responses:
     *  - 200: Bénéficiaire mis à jour avec succès
     *  - 404: Ressource non trouvée
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @param id 
     * @param beneficiariesIdPutRequest 
     * @return [Beneficiary]
     */
    @PUT("beneficiaries/{id}")
    suspend fun beneficiariesIdPut(@Path("id") id: kotlin.Int, @Body beneficiariesIdPutRequest: BeneficiariesIdPutRequest): Response<Beneficiary>

    /**
     * POST beneficiaries
     * Créer un nouveau bénéficiaire
     * Use Case 6 (Client): Ajouter un bénéficiaire
     * Responses:
     *  - 201: Bénéficiaire créé avec succès
     *  - 400: Requête invalide
     *  - 401: Non autorisé - Token d'authentification manquant ou invalide
     *
     * @param beneficiariesPostRequest 
     * @return [Unit]
     */
    @POST("beneficiaries")
    suspend fun beneficiariesPost(@Body beneficiariesPostRequest: BeneficiariesPostRequest): Response<Unit>

}
