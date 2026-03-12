package defalt.network.api.bank.service

import defalt.network.api.bank.model.BankAccountParameter
import defalt.network.infrastructure.CollectionFormats.*
import retrofit2.Response
import retrofit2.http.*

interface BankAccountParameterApi {
    /**
     * PATCH bank/admin/bank-accounts/{bank_account_id}/parameters
     * [ADMIN] Mettre à jour les paramètres
     *
     * Responses:
     *  - 200: Paramètres mis à jour
     *  - 400: Requête invalide
     *  - 404: Ressource non trouvée
     *
     * @param bankAccountId
     * @param bankAccountParameter
     * @return [Unit]
     */
    @PUT("bank/admin/bank-accounts/{bank_account_id}/parameters")
    suspend fun bankAdminBankAccountsBankAccountIdParametersPatch(@Path("bank_account_id") bankAccountId: kotlin.String, @Body bankAccountParameter: BankAccountParameter): Response<Unit>
}
