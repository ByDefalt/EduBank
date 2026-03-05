package defalt.domain.repository.service

import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.bank.BankAccountCreateRequest
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.BankAccountParameter
import defalt.utils.NetworkResult

interface IBankRepository {
    // --- ADMIN ---
    suspend fun adminGetBankAccountsByAccountId(accountId: Int): NetworkResult<List<BankAccount>>
    suspend fun adminCreateBankAccount(accountId: Int, request: BankAccountCreateRequest): NetworkResult<BankAccountDetail>
    suspend fun adminGetAllBankAccounts(): NetworkResult<List<BankAccount>>
    suspend fun adminDeleteBankAccount(id: String): NetworkResult<Unit>
    suspend fun adminGetBankAccountById(id: String): NetworkResult<BankAccountDetail>
    suspend fun adminUpdateBankAccountParameters(bankAccountId: String, parameter: BankAccountParameter): NetworkResult<Unit>

    // --- CLIENT ---
    suspend fun getMyBankAccounts(typeId: Int? = null): NetworkResult<List<BankAccount>>
    suspend fun getMyBankAccountById(id: String): NetworkResult<BankAccountDetail>
    suspend fun getMyBankAccountCoHolders(id: String): NetworkResult<List<Int>>
}
