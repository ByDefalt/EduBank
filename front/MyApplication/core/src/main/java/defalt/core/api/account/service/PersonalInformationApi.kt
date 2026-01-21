package defalt.core.api.account.service

import defalt.core.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import defalt.core.api.account.model.PersonalInformation
import defalt.core.api.account.model.PersonalInformationRegister

interface PersonalInformationApi {
    /**
     * GET personal-information
     * Liste toutes les informations personnelles
     * 
     * Responses:
     *  - 200: Succès
     *
     * @return [kotlin.collections.List<PersonalInformation>]
     */
    @GET("personal-information")
    suspend fun personalInformationGet(): Response<kotlin.collections.List<PersonalInformation>>

    /**
     * GET personal-information/{id}
     * Récupérer une info personnelle par ID
     * 
     * Responses:
     *  - 200: Trouvé
     *
     * @param id 
     * @return [PersonalInformation]
     */
    @GET("personal-information/{id}")
    suspend fun personalInformationIdGet(@Path("id") id: kotlin.Int): Response<PersonalInformation>

    /**
     * POST personal-information
     * Créer une fiche d&#39;information personnelle
     * 
     * Responses:
     *  - 201: Créé avec succès
     *
     * @param personalInformationRegister 
     * @return [PersonalInformation]
     */
    @POST("personal-information")
    suspend fun personalInformationPost(@Body personalInformationRegister: PersonalInformationRegister): Response<PersonalInformation>

}
