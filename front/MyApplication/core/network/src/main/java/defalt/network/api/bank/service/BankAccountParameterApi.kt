package defalt.network.api.bank.service

import defalt.network.api.bank.model.BankAccountParameter
import defalt.network.infrastructure.CollectionFormats.*
import retrofit2.Response
import retrofit2.http.*

interface BankAccountParameterApi {
    /**
     * PATCH admin/bank-accounts/{bank_account_id}/parameters
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
    @PATCH("admin/bank-accounts/{bank_account_id}/parameters")
    suspend fun adminBankAccountsBankAccountIdParametersPatch(@Path("bank_account_id") bankAccountId: kotlin.String, @Body bankAccountParameter: BankAccountParameter): Response<Unit>
}
