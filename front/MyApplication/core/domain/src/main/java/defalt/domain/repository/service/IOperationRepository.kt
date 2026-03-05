package defalt.domain.repository.service

import defalt.domain.entity.operation.Beneficiary
import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import defalt.utils.NetworkResult
import java.time.OffsetDateTime

interface IOperationRepository {
    // --- OPÉRATIONS ---
    suspend fun getOperations(
        accountSourceId: String? = null,
        state: OperationState? = null,
        dateFrom: OffsetDateTime? = null,
        dateTo: OffsetDateTime? = null,
    ): NetworkResult<List<Operation>>
    suspend fun getOperationById(id: Int): NetworkResult<Operation>
    suspend fun createOperation(operation: Operation): NetworkResult<Operation>
    suspend fun cancelOperation(id: Int): NetworkResult<Operation>
    suspend fun updateOperationState(id: Int, state: String): NetworkResult<Operation>

    // --- BÉNÉFICIAIRES ---
    suspend fun getAllBeneficiaries(): NetworkResult<List<Beneficiary>>
    suspend fun getBeneficiariesByAccountId(accountId: String): NetworkResult<List<Beneficiary>>
    suspend fun createBeneficiary(beneficiary: Beneficiary): NetworkResult<Beneficiary>
    suspend fun updateBeneficiary(id: Int, beneficiary: Beneficiary): NetworkResult<Beneficiary>
    suspend fun deleteBeneficiary(id: Int): NetworkResult<Unit>
}
