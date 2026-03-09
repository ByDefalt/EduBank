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
    ): NetworkResult<List<Operation>> =
        remoteDataSource.getOperations(accountSourceId, state, dateFrom, dateTo)

    override suspend fun getOperationById(id: Int): NetworkResult<Operation> =
        remoteDataSource.getOperationById(id)

    override suspend fun createOperation(operation: Operation): NetworkResult<Operation> =
        remoteDataSource.createOperation(operation)

    override suspend fun cancelOperation(id: Int): NetworkResult<Operation> =
        remoteDataSource.cancelOperation(id)

    override suspend fun updateOperationState(id: Int, state: String): NetworkResult<Operation> =
        remoteDataSource.updateOperationState(id, state)

    // --- BÉNÉFICIAIRES ---

    override suspend fun getAllBeneficiaries(): NetworkResult<List<Beneficiary>> =
        remoteDataSource.getAllBeneficiaries()

    override suspend fun getBeneficiariesByAccountId(accountId: String): NetworkResult<List<Beneficiary>> =
        remoteDataSource.getBeneficiariesByAccountId(accountId)

    override suspend fun createBeneficiary(beneficiary: Beneficiary): NetworkResult<Beneficiary> =
        remoteDataSource.createBeneficiary(beneficiary)

    override suspend fun updateBeneficiary(id: Int, beneficiary: Beneficiary): NetworkResult<Beneficiary> =
        remoteDataSource.updateBeneficiary(id, beneficiary)

    override suspend fun deleteBeneficiary(id: Int): NetworkResult<Unit> =
        remoteDataSource.deleteBeneficiary(id)
}
