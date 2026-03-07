package defalt.network.fake

import defalt.domain.datasource.bank.IBankRemoteDataSource
import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.bank.BankAccountCreateRequest
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.BankAccountParameter
import defalt.domain.entity.bank.State
import defalt.domain.entity.bank.Type
import defalt.utils.NetworkResult

class FakeBankRemoteDataSource : IBankRemoteDataSource {

    private val bankAccounts = FakeData.bankAccounts.toMutableList()
    private val bankAccountDetails = FakeData.bankAccountDetails.toMutableList()
    private val bankParameters = FakeData.bankParameters.toMutableList()

    // --- ADMIN ---

    override suspend fun adminGetBankAccountsByAccountId(accountId: Int): NetworkResult<List<BankAccount>> =
        NetworkResult.Success(bankAccounts.toList())

    override suspend fun adminGetAllBankAccounts(): NetworkResult<List<BankAccount>> =
        NetworkResult.Success(bankAccounts.toList())

    override suspend fun adminGetBankAccountById(id: String): NetworkResult<BankAccountDetail> =
        bankAccountDetails.find { it.id == id }
            ?.let { NetworkResult.Success(it) }
            ?: NetworkResult.Error(code = 404, message = "Compte bancaire introuvable : $id")

    override suspend fun adminCreateBankAccount(
        accountId: Int,
        request: BankAccountCreateRequest,
    ): NetworkResult<BankAccountDetail> {
        val newParam = BankAccountParameter(
            id = bankParameters.size + 1,
            overdraftLimit = request.overdraftLimit,
            state = request.state ?: State.ACTIVE,
        )
        val newType = Type(id = request.typeId, name = "Type ${ request.typeId }")
        val newDetail = BankAccountDetail(
            id = "bank-${(bankAccountDetails.size + 1).toString().padStart(4, '0')}",
            parameter = newParam,
            type = newType,
            sold = request.sold,
            iban = request.iban,
        )
        val newAccount = BankAccount(
            id = newDetail.id,
            parameterId = newParam.id,
            typeId = request.typeId,
            sold = request.sold,
            iban = request.iban,
        )
        bankParameters.add(newParam)
        bankAccounts.add(newAccount)
        bankAccountDetails.add(newDetail)
        return NetworkResult.Success(newDetail)
    }

    override suspend fun adminDeleteBankAccount(id: String): NetworkResult<Unit> =
        if (bankAccounts.removeIf { it.id == id } && bankAccountDetails.removeIf { it.id == id }) {
            NetworkResult.Success(Unit)
        } else {
            NetworkResult.Error(code = 404, message = "Compte bancaire introuvable : $id")
        }

    override suspend fun adminUpdateBankAccountParameters(
        bankAccountId: String,
        parameter: BankAccountParameter,
    ): NetworkResult<Unit> {
        val index = bankAccountDetails.indexOfFirst { it.id == bankAccountId }
        return if (index != -1) {
            bankAccountDetails[index] = bankAccountDetails[index].copy(parameter = parameter)
            NetworkResult.Success(Unit)
        } else {
            NetworkResult.Error(code = 404, message = "Compte bancaire introuvable : $bankAccountId")
        }
    }

    // --- CLIENT ---

    override suspend fun getMyBankAccounts(typeId: Int?): NetworkResult<List<BankAccount>> {
        val result = if (typeId != null) {
            bankAccounts.filter { it.typeId == typeId }
        } else bankAccounts.toList()
        return NetworkResult.Success(result)
    }

    override suspend fun getMyBankAccountById(id: String): NetworkResult<BankAccountDetail> =
        bankAccountDetails.find { it.id == id }
            ?.let { NetworkResult.Success(it) }
            ?: NetworkResult.Error(code = 404, message = "Compte bancaire introuvable : $id")

    override suspend fun getMyBankAccountCoHolders(id: String): NetworkResult<List<Int>> =
        NetworkResult.Success(emptyList())
}
