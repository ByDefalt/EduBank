package defalt.feature_bank.repository.service

import defalt.core.api.bank.model.BankAccount
import defalt.core.api.bank.model.BankAccountDetails
import defalt.core.api.bank.model.BankAccountsIdBalanceGet200Response
import defalt.core.api.bank.model.BankAccountsIdPutRequest
import defalt.core.api.bank.model.BankAccountsIdStatePatchRequest
import defalt.core.api.bank.model.BankAccountsPostRequest
import defalt.core.utils.NetworkResult

interface IBankAccountRepository {

    suspend fun getCurrentBankAccount(): NetworkResult<BankAccountDetails>

    suspend fun getBankAccounts(
        accountId: Int? = null,
        typeId: Int? = null,
        state: String? = null
    ): NetworkResult<List<BankAccountDetails>>

    suspend fun getBankAccountById(id: Int): NetworkResult<BankAccountDetails>

    suspend fun getBankAccountBalance(id: Int): NetworkResult<BankAccountsIdBalanceGet200Response>

    suspend fun createBankAccount(request: BankAccountsPostRequest): NetworkResult<BankAccount>

    suspend fun updateBankAccount(id: Int, request: BankAccountsIdPutRequest): NetworkResult<BankAccount>

    suspend fun changeBankAccountState(id: Int, request: BankAccountsIdStatePatchRequest): NetworkResult<BankAccount>

    suspend fun deleteBankAccount(id: Int): NetworkResult<Unit>
}
