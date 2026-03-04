package defalt.network.api.bank.service

import defalt.network.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import defalt.network.api.bank.model.BankAccountParameter
import defalt.network.api.bank.model.MessageResponse

interface BankAccountParameterApi {
    /**
     * PATCH admin/bank-accounts/{bank_account_id}/parameters
     * [ADMIN] Mettre à jour les paramètres d&#39;un compte
     * Permet de modifier le découvert autorisé et/ou l&#39;état du compte. On peut modifier un seul paramètre ou les deux en même temps. 
     * Responses:
     *  - 200: Paramètres mis à jour
     *  - 400: Données invalides
     *  - 404: Compte non trouvé
     *
     * @param bankAccountId 
     * @param bankAccountParameter 
     * @return [MessageResponse]
     */
    @PATCH("admin/bank-accounts/{bank_account_id}/parameters")
    suspend fun adminBankAccountsBankAccountIdParametersPatch(@Path("bank_account_id") bankAccountId: kotlin.String, @Body bankAccountParameter: BankAccountParameter): Response<MessageResponse>

}
