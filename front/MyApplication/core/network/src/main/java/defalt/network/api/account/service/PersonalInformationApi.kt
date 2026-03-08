package defalt.network.api.account.service

import defalt.network.api.account.model.PersonalInformation
import defalt.network.api.account.model.PersonalInformationRegister
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PersonalInformationApi {
    /**
     * GET personalInformation
     * Liste toutes les informations personnelles (Admin uniquement)
     *
     * Responses:
     *  - 200: Succès
     *  - 401: Non autorisé
     *  - 403: Interdit
     *
     * @return [kotlin.collections.List<PersonalInformation>]
     */
    @GET("personalInformation")
    suspend fun personalInformationGet(): Response<kotlin.collections.List<PersonalInformation>>

    /**
     * GET personalInformation/{id}
     * Récupérer une info personnelle par ID
     *
     * Responses:
     *  - 200: Trouvé
     *  - 404: Non trouvé
     *
     * @param id
     * @return [PersonalInformation]
     */
    @GET("personalInformation/{id}")
    suspend fun personalInformationIdGet(@Path("id") id: kotlin.Int): Response<PersonalInformation>

    /**
     * POST personalInformation
     * Créer une fiche d&#39;information personnelle
     *
     * Responses:
     *  - 201: Créé avec succès
     *
     * @param personalInformationRegister
     * @return [PersonalInformation]
     */
    @POST("personalInformation")
    suspend fun personalInformationPost(@Body personalInformationRegister: PersonalInformationRegister): Response<PersonalInformation>

    @PUT("personalInformation/{id}")
    suspend fun personalInformationIdPut(@Path("id") id: Int, @Body personalInformation: PersonalInformation): Response<PersonalInformation>
}
