package defalt.feature_bank.repository.impl

import defalt.core.api.bank.model.*
import defalt.core.api.bank.service.BankAccountApi
import defalt.core.utils.NetworkResult
import defalt.core.utils.safeApiCall
import defalt.feature_bank.repository.service.IBankAccountRepository

class BankAccountRepository(
    private val api: BankAccountApi
) : IBankAccountRepository {

    override suspend fun getCurrentBankAccount(): NetworkResult<BankAccountDetails> {
        return safeApiCall { api.bankAccountsCurrentGet() }
    }

    override suspend fun getBankAccounts(
        accountId: Int?,
        typeId: Int?,
        state: String?
    ): NetworkResult<List<BankAccountDetails>> {
        return safeApiCall { api.bankAccountsGet(accountId, typeId, state) }
    }

    override suspend fun getBankAccountById(id: Int): NetworkResult<BankAccountDetails> {
        return safeApiCall { api.bankAccountsIdGet(id) }
    }

    override suspend fun getBankAccountBalance(id: Int): NetworkResult<BankAccountsIdBalanceGet200Response> {
        return safeApiCall { api.bankAccountsIdBalanceGet(id) }
    }

    override suspend fun createBankAccount(request: BankAccountsPostRequest): NetworkResult<BankAccount> {
        return safeApiCall { api.bankAccountsPost(request) }
    }

    override suspend fun updateBankAccount(id: Int, request: BankAccountsIdPutRequest): NetworkResult<BankAccount> {
        return safeApiCall { api.bankAccountsIdPut(id, request) }
    }

    override suspend fun changeBankAccountState(id: Int, request: BankAccountsIdStatePatchRequest): NetworkResult<BankAccount> {
        return safeApiCall { api.bankAccountsIdStatePatch(id, request) }
    }

    override suspend fun deleteBankAccount(id: Int): NetworkResult<Unit> {
        return safeApiCall { api.bankAccountsIdDelete(id) }
    }
}
