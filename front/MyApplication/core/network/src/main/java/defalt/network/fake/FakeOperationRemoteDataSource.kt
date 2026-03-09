package defalt.network.fake

import defalt.domain.datasource.operation.IOperationRemoteDataSource
import defalt.domain.entity.operation.Beneficiary
import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import defalt.utils.NetworkResult
import java.time.OffsetDateTime

class FakeOperationRemoteDataSource : IOperationRemoteDataSource {

    private val operations = FakeData.operations.toMutableList()
    private val beneficiaries = FakeData.beneficiaries.toMutableList()

    // --- OPÉRATIONS ---

    override suspend fun getOperations(
        accountSourceId: String?,
        state: OperationState?,
        dateFrom: OffsetDateTime?,
        dateTo: OffsetDateTime?,
    ): NetworkResult<List<Operation>> {
        var result = operations.toList()
        if (accountSourceId != null) result = result.filter { it.accountSourceId == accountSourceId }
        if (state != null) result = result.filter { it.state == state }
        if (dateFrom != null) result = result.filter { it.date >= dateFrom }
        if (dateTo != null) result = result.filter { it.date <= dateTo }
        return NetworkResult.Success(result)
    }

    override suspend fun getOperationById(id: Int): NetworkResult<Operation> =
        operations.find { it.id == id }
            ?.let { NetworkResult.Success(it) }
            ?: NetworkResult.Error(code = 404, message = "Opération introuvable : $id")

    override suspend fun createOperation(operation: Operation): NetworkResult<Operation> {
        val newOperation = operation.copy(
            id = operations.size + 1,
            state = OperationState.PENDING,
        )
        operations.add(newOperation)
        return NetworkResult.Success(newOperation)
    }

    override suspend fun cancelOperation(id: Int): NetworkResult<Operation> {
        val index = operations.indexOfFirst { it.id == id }
        return if (index != -1) {
            val cancelled = operations[index].copy(state = OperationState.CANCELLED)
            operations[index] = cancelled
            NetworkResult.Success(cancelled)
        } else {
            NetworkResult.Error(code = 404, message = "Opération introuvable : $id")
        }
    }

    override suspend fun updateOperationState(id: Int, state: String): NetworkResult<Operation> {
        val index = operations.indexOfFirst { it.id == id }
        return if (index != -1) {
            val newState = OperationState.entries.find { it.value == state }
                ?: return NetworkResult.Error(code = 400, message = "État invalide : $state")
            val updated = operations[index].copy(state = newState)
            operations[index] = updated
            NetworkResult.Success(updated)
        } else {
            NetworkResult.Error(code = 404, message = "Opération introuvable : $id")
        }
    }

    // --- BÉNÉFICIAIRES ---

    override suspend fun getAllBeneficiaries(): NetworkResult<List<Beneficiary>> =
        NetworkResult.Success(beneficiaries.toList())

    override suspend fun getBeneficiariesByAccountId(accountId: String): NetworkResult<List<Beneficiary>> =
        NetworkResult.Success(beneficiaries.filter { it.accountSourceId == accountId })

    override suspend fun createBeneficiary(beneficiary: Beneficiary): NetworkResult<Beneficiary> {
        val newBeneficiary = beneficiary.copy(id = beneficiaries.size + 1)
        beneficiaries.add(newBeneficiary)
        return NetworkResult.Success(newBeneficiary)
    }

    override suspend fun updateBeneficiary(id: Int, beneficiary: Beneficiary): NetworkResult<Beneficiary> {
        val index = beneficiaries.indexOfFirst { it.id == id }
        return if (index != -1) {
            val updated = beneficiary.copy(id = id)
            beneficiaries[index] = updated
            NetworkResult.Success(updated)
        } else {
            NetworkResult.Error(code = 404, message = "Bénéficiaire introuvable : $id")
        }
    }

    override suspend fun deleteBeneficiary(id: Int): NetworkResult<Unit> =
        if (beneficiaries.removeIf { it.id == id }) {
            NetworkResult.Success(Unit)
        } else NetworkResult.Error(code = 404, message = "Bénéficiaire introuvable : $id")
}
