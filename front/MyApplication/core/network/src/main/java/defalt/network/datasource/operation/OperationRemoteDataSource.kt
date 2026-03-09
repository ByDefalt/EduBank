package defalt.network.datasource.operation

import defalt.domain.datasource.operation.IOperationRemoteDataSource
import defalt.domain.entity.operation.Beneficiary
import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import defalt.network.api.operation.model.OperationFilter
import defalt.network.api.operation.service.BeneficiaryApi
import defalt.network.api.operation.service.OperationApi
import defalt.network.mapper.operation.toDto
import defalt.network.mapper.operation.toEntity
import defalt.network.utils.safeApiCall
import defalt.utils.NetworkResult
import defalt.utils.map
import java.time.OffsetDateTime

class OperationRemoteDataSource(
    private val operationApi: OperationApi,
    private val beneficiaryApi: BeneficiaryApi,
) : IOperationRemoteDataSource {

    // --- OPÉRATIONS ---

    override suspend fun getOperations(
        accountSourceId: String?,
        state: OperationState?,
        dateFrom: OffsetDateTime?,
        dateTo: OffsetDateTime?,
    ): NetworkResult<List<Operation>> {
        val stateDto = state?.toDto()
        val filter = OperationFilter(
            state = stateDto,
            dateFrom = dateFrom,
            dateTo = dateTo,
        )
        return if (accountSourceId != null) {
            safeApiCall { operationApi.operationsAccountAccountIdGet(accountSourceId, filter) }
                .map { it.data?.toEntity() ?: emptyList() }
        } else {
            safeApiCall { operationApi.operationsGet(filter) }
                .map { it.data?.toEntity() ?: emptyList() }
        }
    }

    override suspend fun getOperationById(id: Int): NetworkResult<Operation> =
        safeApiCall { operationApi.operationsIdGet(id) }
            .map { it.toEntity() }

    override suspend fun createOperation(operation: Operation): NetworkResult<Operation> =
        safeApiCall { operationApi.operationsPost(operation.toDto()) }
            .map { it.toEntity() }

    override suspend fun cancelOperation(id: Int): NetworkResult<Operation> =
        safeApiCall { operationApi.operationsIdCancelPost(id) }
            .map { it.toEntity() }

    override suspend fun updateOperationState(id: Int, state: String): NetworkResult<Operation> =
        safeApiCall { operationApi.operationsIdStatePatch(id, "\"$state\"") }
            .map { it.toEntity() }

    // --- BÉNÉFICIAIRES ---

    override suspend fun getAllBeneficiaries(): NetworkResult<List<Beneficiary>> =
        safeApiCall { beneficiaryApi.beneficiariesGet() }
            .map { it.data?.toEntity() ?: emptyList() }

    override suspend fun getBeneficiariesByAccountId(accountId: String): NetworkResult<List<Beneficiary>> =
        safeApiCall { beneficiaryApi.beneficiariesAccountIdGet(accountId) }
            .map { it.data?.toEntity() ?: emptyList() }

    override suspend fun createBeneficiary(beneficiary: Beneficiary): NetworkResult<Beneficiary> =
        safeApiCall { beneficiaryApi.beneficiariesPost(beneficiary.toDto()) }
            .map { it.toEntity() }

    override suspend fun updateBeneficiary(id: Int, beneficiary: Beneficiary): NetworkResult<Beneficiary> =
        safeApiCall { beneficiaryApi.beneficiariesIdPut(id, beneficiary.toDto()) }
            .map { it.toEntity() }

    override suspend fun deleteBeneficiary(id: Int): NetworkResult<Unit> =
        safeApiCall { beneficiaryApi.beneficiariesIdDelete(id) }
}
