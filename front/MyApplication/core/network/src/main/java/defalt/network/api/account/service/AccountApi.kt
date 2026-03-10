package defalt.network.api.account.service

import defalt.network.api.account.model.Account
import defalt.network.api.account.model.AccountRegister
import defalt.network.api.account.model.PersonalInformation
import defalt.network.api.account.model.Role
import defalt.network.api.account.model.SignInRequest
import defalt.network.api.account.model.TokenRequest
import defalt.network.api.account.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AccountApi {
    /**
     * PUT accounts/activate/{id}
     * Activer un compte (Admin uniquement)
     *
     * Responses:
     *  - 200: Compte activé avec succès
     *  - 400: Compte déjà actif ou clôturé
     *  - 404: Compte non trouvé
     *  - 401: Non autorisé
     *  - 403: Interdit
     *
     * @param id
     * @return [kotlin.Boolean]
     */
    @PUT("accounts/activate/{id}")
    suspend fun accountsActivateIdPut(@Path("id") id: kotlin.String): Response<kotlin.Boolean>

    /**
     * PUT accounts/deactivate/{id}
     * Désactiver un compte (Admin uniquement)
     *
     * Responses:
     *  - 200: Compte désactivé avec succès
     *  - 400: Compte déjà inactif ou clôturé
     *  - 404: Compte non trouvé
     *  - 401: Non autorisé
     *  - 403: Interdit
     *
     * @param id
     * @return [kotlin.Boolean]
     */
    @PUT("accounts/deactivate/{id}")
    suspend fun accountsDeactivateIdPut(@Path("id") id: kotlin.String): Response<kotlin.Boolean>

    /**
     * GET accounts
     * Liste tous les comptes (Admin uniquement)
     *
     * Responses:
     *  - 200: Succès
     *  - 401: Non autorisé
     *  - 403: Interdit
     *
     * @return [kotlin.collections.List<Account>]
     */
    @GET("accounts")
    suspend fun accountsGet(): Response<kotlin.collections.List<Account>>

    /**
     * GET accounts/{id}
     * Récupérer un compte par ID
     *
     * Responses:
     *  - 200: Compte trouvé
     *  - 404: Compte non trouvé
     *
     * @param id
     * @return [Account]
     */
    @GET("accounts/{id}")
    suspend fun accountsIdGet(@Path("id") id: kotlin.String): Response<Account>

    /**
     * PUT accounts/{id}
     * Supprimer un compte (Admin uniquement, par le changement d&#39;etat du compte à ENCLOSE)
     *
     * Responses:
     *  - 200: Compte supprimé avec succès (retourne true)
     *  - 404: Compte non trouvé
     *  - 401: Non autorisé
     *  - 403: Interdit
     *
     * @param id
     * @return [kotlin.Boolean]
     */
    @PUT("accounts/{id}")
    suspend fun accountsIdPut(@Path("id") id: kotlin.String): Response<kotlin.Boolean>

    /**
     * GET accounts/personalInformation/{id}
     * Récupérer les informations personnelles associées à un compte
     *
     * Responses:
     *  - 200: Infos trouvées
     *  - 404: Compte ou Infos non trouvées
     *
     * @param id ID du compte
     * @return [PersonalInformation]
     */
    @GET("accounts/personalInformation/{id}")
    suspend fun accountsPersonalInformationIdGet(@Path("id") id: kotlin.String): Response<PersonalInformation>

    /**
     * POST accounts
     * Créer un nouveau compte
     * Crée le compte, le rôle et les informations personnelles liées.
     * Responses:
     *  - 201: Compte créé
     *  - 400: Impossible de créer le compte (rôle inexistant, erreur personnelle info, etc.)
     *  - 404: Rôle non trouvé
     *
     * @param accountRegister
     * @return [Account]
     */
    @POST("accounts")
    suspend fun accountsPost(@Body accountRegister: AccountRegister): Response<Account>

    /**
     * GET accounts/role/{id}
     * Récupérer le rôle associé à un compte
     *
     * Responses:
     *  - 200: Rôle trouvé
     *  - 404: Compte ou Rôle non trouvé
     *
     * @param id ID du compte
     * @return [Role]
     */
    @GET("accounts/role/{id}")
    suspend fun accountsRoleIdGet(@Path("id") id: kotlin.String): Response<Role>

    /**
     * POST accounts/signin
     * Connexion utilisateur
     *
     * Responses:
     *  - 200: Connexion réussie, retourne le token
     *  - 404: Compte non trouvé
     *  - 401: Mot de passe incorrect ou compte inactif
     *
     * @param signInRequest
     * @return [TokenRequest]
     */
    @POST("accounts/signin")
    suspend fun accountsSigninPost(@Body signInRequest: SignInRequest): Response<TokenRequest>

    /**
     * POST accounts/validate
     * Valider un token JWT
     *
     * Responses:
     *  - 200: Token valide
     *  - 401: Token invalide ou expiré
     *
     * @param tokenRequest
     * @return [TokenResponse]
     */
    @POST("accounts/validate")
    suspend fun accountsValidatePost(@Body tokenRequest: TokenRequest): Response<TokenResponse>
}
