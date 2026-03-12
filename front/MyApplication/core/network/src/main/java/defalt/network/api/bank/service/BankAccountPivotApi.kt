package defalt.network.api.bank.service

import defalt.network.api.bank.model.BankAccountPivot
import defalt.network.infrastructure.CollectionFormats.*
import retrofit2.Response
import retrofit2.http.*

interface BankAccountPivotApi {
    /**
     * DELETE bank/bank-accounts-pivot/account/{accountId}
     * [ADMIN] Retirer tous les comptes d&#39;un user
     *
     * Responses:
     *  - 204: Tous les liens supprimés
     *
     * @param accountId
     * @return [Unit]
     */
    @DELETE("bank/bank-accounts-pivot/account/{accountId}")
    suspend fun bankBankAccountsPivotAccountAccountIdDelete(@Path("accountId") accountId: kotlin.String): Response<Unit>

    /**
     * GET bank/bank-accounts-pivot/account/{accountId}
     * [CLIENT] Mes liens de comptes
     *
     * Responses:
     *  - 200: Liste des liens
     *
     * @param accountId
     * @return [kotlin.collections.List<BankAccountPivot>]
     */
    @GET("bank/bank-accounts-pivot/account/{accountId}")
    suspend fun bankBankAccountsPivotAccountAccountIdGet(@Path("accountId") accountId: kotlin.String): Response<kotlin.collections.List<BankAccountPivot>>

    /**
     * DELETE bank/bank-accounts-pivot/bank-account/{bankAccountId}
     * [ADMIN] Retirer tous les co-titulaires
     *
     * Responses:
     *  - 204: Tous les liens supprimés
     *
     * @param bankAccountId
     * @return [Unit]
     */
    @DELETE("bank/bank-accounts-pivot/bank-account/{bankAccountId}")
    suspend fun bankBankAccountsPivotBankAccountBankAccountIdDelete(@Path("bankAccountId") bankAccountId: kotlin.String): Response<Unit>

    /**
     * GET bank/bank-accounts-pivot/bank-account/{bankAccountId}
     * [ADMIN] Co-titulaires d&#39;un compte
     *
     * Responses:
     *  - 200: Liste des liens
     *
     * @param bankAccountId
     * @return [kotlin.collections.List<BankAccountPivot>]
     */
    @GET("bank/bank-accounts-pivot/bank-account/{bankAccountId}")
    suspend fun bankBankAccountsPivotBankAccountBankAccountIdGet(@Path("bankAccountId") bankAccountId: kotlin.String): Response<kotlin.collections.List<BankAccountPivot>>

    /**
     * DELETE bank/bank-accounts-pivot
     * [ADMIN] Retirer un co-titulaire
     *
     * Responses:
     *  - 204: Lien supprimé
     *  - 404: Ressource non trouvée
     *
     * @param bankAccountPivot
     * @return [Unit]
     */
    @DELETE("bank/bank-accounts-pivot")
    suspend fun bankBankAccountsPivotDelete(@Body bankAccountPivot: BankAccountPivot): Response<Unit>

    /**
     * POST bank/bank-accounts-pivot
     * [ADMIN] Ajouter un co-titulaire
     *
     * Responses:
     *  - 201: Lien créé
     *  - 400: Requête invalide
     *
     * @param bankAccountPivot
     * @return [Unit]
     */
    @POST("bank/bank-accounts-pivot")
    suspend fun bankBankAccountsPivotPost(@Body bankAccountPivot: BankAccountPivot): Response<Unit>
}
