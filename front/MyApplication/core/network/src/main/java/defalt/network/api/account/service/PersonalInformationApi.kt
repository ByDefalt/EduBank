package defalt.network.api.account.service

import defalt.network.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import defalt.network.api.account.model.PersonalInformation
import defalt.network.api.account.model.PersonalInformationRegister

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

}
