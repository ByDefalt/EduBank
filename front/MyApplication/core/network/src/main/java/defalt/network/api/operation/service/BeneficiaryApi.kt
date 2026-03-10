package defalt.network.api.operation.service

import defalt.network.api.operation.model.Beneficiary
import defalt.network.api.operation.model.BeneficiaryList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BeneficiaryApi {
    /**
     * GET beneficiaries/{accountId}
     * Récupérer les bénéficiaires d&#39;un compte
     * Retourne tous les bénéficiaires associés à un compte source spécifique
     * Responses:
     *  - 200: Liste des bénéficiaires du compte
     *  - 404: Ressource non trouvée
     *
     * @param accountId ID du compte source (ex: 1)
     * @return [BeneficiaryList]
     */
    @GET("beneficiaries/{accountId}")
    suspend fun beneficiariesAccountIdGet(@Path("accountId") accountId: kotlin.String): Response<BeneficiaryList>

    /**
     * GET beneficiaries
     * Récupérer tous les bénéficiaires
     *
     * Responses:
     *  - 200: Liste globale des bénéficiaires
     *
     * @return [BeneficiaryList]
     */
    @GET("beneficiaries")
    suspend fun beneficiariesGet(): Response<BeneficiaryList>

    /**
     * DELETE beneficiaries/{id}
     * Supprimer un bénéficiaire
     *
     * Responses:
     *  - 204: Supprimé avec succès
     *
     * @param id
     * @return [Unit]
     */
    @DELETE("beneficiaries/{id}")
    suspend fun beneficiariesIdDelete(@Path("id") id: kotlin.Int): Response<Unit>

    /**
     * PUT beneficiaries/{id}
     * Mettre à jour un bénéficiaire
     *
     * Responses:
     *  - 200: Bénéficiaire mis à jour
     *
     * @param id
     * @param beneficiary
     * @return [Beneficiary]
     */
    @PUT("beneficiaries/{id}")
    suspend fun beneficiariesIdPut(@Path("id") id: kotlin.Int, @Body beneficiary: Beneficiary): Response<Beneficiary>

    /**
     * POST beneficiaries
     * Créer un nouveau bénéficiaire
     *
     * Responses:
     *  - 201: Bénéficiaire créé
     *
     * @param beneficiary
     * @return [Beneficiary]
     */
    @POST("beneficiaries")
    suspend fun beneficiariesPost(@Body beneficiary: Beneficiary): Response<Beneficiary>
}
