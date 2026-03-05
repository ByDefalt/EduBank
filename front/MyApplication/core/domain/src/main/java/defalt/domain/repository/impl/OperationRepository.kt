package defalt.domain.repository.impl

import defalt.domain.datasource.operation.IOperationRemoteDataSource
import defalt.domain.entity.operation.Beneficiary
import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import defalt.domain.repository.service.IOperationRepository
import defalt.utils.NetworkResult
import java.time.OffsetDateTime

class OperationRepository(
    private val remoteDataSource: IOperationRemoteDataSource,
) : IOperationRepository {

    // --- OPÉRATIONS ---

    override suspend fun getOperations(
        accountSourceId: String?,
        state: OperationState?,
        dateFrom: OffsetDateTime?,
        dateTo: OffsetDateTime?,
    ): NetworkResult<List<Operation>> {
        TODO("Not yet implemented")
    }

    override suspend fun getOperationById(id: Int): NetworkResult<Operation> {
        TODO("Not yet implemented")
    }

    override suspend fun createOperation(operation: Operation): NetworkResult<Operation> {
        TODO("Not yet implemented")
    }

    override suspend fun cancelOperation(id: Int): NetworkResult<Operation> {
        TODO("Not yet implemented")
    }

    override suspend fun updateOperationState(id: Int, state: String): NetworkResult<Operation> {
        TODO("Not yet implemented")
    }

    // --- BÉNÉFICIAIRES ---

    override suspend fun getAllBeneficiaries(): NetworkResult<List<Beneficiary>> {
        TODO("Not yet implemented")
    }

    override suspend fun getBeneficiariesByAccountId(accountId: String): NetworkResult<List<Beneficiary>> {
        TODO("Not yet implemented")
    }

    override suspend fun createBeneficiary(beneficiary: Beneficiary): NetworkResult<Beneficiary> {
        TODO("Not yet implemented")
    }

    override suspend fun updateBeneficiary(id: Int, beneficiary: Beneficiary): NetworkResult<Beneficiary> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBeneficiary(id: Int): NetworkResult<Unit> {
        TODO("Not yet implemented")
    }
}
