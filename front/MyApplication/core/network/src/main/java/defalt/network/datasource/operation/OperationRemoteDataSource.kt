package defalt.network.datasource.operation

import defalt.domain.datasource.operation.IOperationRemoteDataSource
import defalt.domain.entity.operation.Beneficiary
import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import defalt.network.api.operation.model.BeneficiaryList
import defalt.network.api.operation.service.BeneficiaryApi
import defalt.network.api.operation.service.OperationApi
import defalt.network.mapper.operation.toDto
import defalt.network.mapper.operation.toEntity
import defalt.network.utils.safeApiCall
import defalt.network.utils.safeApiCallList
import defalt.utils.NetworkResult
import defalt.utils.map
import java.time.OffsetDateTime
import kotlin.collections.emptyList

class OperationRemoteDataSource(
    private val operationApi: OperationApi,
    private val beneficiaryApi: BeneficiaryApi,
) : IOperationRemoteDataSource {

    // --- OPÉRATIONS ---

    override suspend fun getOperations(
        accountId: String?,
        state: OperationState?,
        dateFrom: OffsetDateTime?,
        dateTo: OffsetDateTime?,
    ): NetworkResult<List<Operation>> {
        val stateDto = state?.let {
            // Conversion enum si nécessaire selon le Mapper
            it.toString().lowercase()
        }
        return if (accountId != null) {
            safeApiCall { operationApi.operationsAccountAccountIdGet(accountId, state?.toDto(), dateFrom, dateTo) }
                .map { it.toEntity() }
        } else {
            safeApiCall { operationApi.operationsGet(state?.toDto(), dateFrom, dateTo) }
                .map { it.toEntity() }
        }
    }

    override suspend fun getOperationById(id: Int): NetworkResult<Operation> =
        safeApiCall { operationApi.operationsIdGet(id) }.map { it.toEntity() }

    override suspend fun createOperation(operation: Operation): NetworkResult<Operation> =
        safeApiCall { operationApi.operationsPost(operation.toDto()) }.map { it.toEntity() }

    override suspend fun cancelOperation(id: Int): NetworkResult<Operation> =
        safeApiCall { operationApi.operationsIdCancelPost(id) }.map { it.toEntity() }

    override suspend fun updateOperationState(id: Int, state: OperationState): NetworkResult<Operation> =
        safeApiCall { operationApi.operationsIdStatePatch(id, state.toString()) }.map { it.toEntity() }

    // --- BÉNÉFICIAIRES ---

    override suspend fun getAllBeneficiaries(): NetworkResult<List<Beneficiary>> =
        safeApiCallList(BeneficiaryList(data = emptyList())) { beneficiaryApi.beneficiariesGet() }.map { it.toEntity() }

    override suspend fun getBeneficiariesByAccountId(accountId: String): NetworkResult<List<Beneficiary>> =
        safeApiCallList(BeneficiaryList(data = emptyList())) { beneficiaryApi.beneficiariesAccountIdGet(accountId) }.map { it.toEntity() }

    override suspend fun createBeneficiary(beneficiary: Beneficiary): NetworkResult<Beneficiary> =
        safeApiCall { beneficiaryApi.beneficiariesPost(beneficiary.toDto()) }.map { it.toEntity() }

    override suspend fun updateBeneficiary(id: Int, beneficiary: Beneficiary): NetworkResult<Beneficiary> =
        safeApiCall { beneficiaryApi.beneficiariesIdPut(id, beneficiary.toDto()) }.map { it.toEntity() }

    override suspend fun deleteBeneficiary(id: Int): NetworkResult<Unit> =
        safeApiCall { beneficiaryApi.beneficiariesIdDelete(id) }
}
