package defalt.feature_operation.repository.impl

import defalt.core.api.operation.model.Operation
import defalt.core.api.operation.model.OperationsGet200Response
import defalt.core.api.operation.model.OperationsIdCancelPost201Response
import defalt.core.api.operation.model.OperationsIdCancelPostRequest
import defalt.core.api.operation.model.OperationsIdStatePatchRequest
import defalt.core.api.operation.model.OperationsPostRequest
import defalt.core.api.operation.service.OperationApi
import defalt.core.api.operation.service.OperationApi.StateOperationsGet
import defalt.core.utils.NetworkResult
import defalt.core.utils.safeApiCall
import defalt.feature_operation.repository.service.IOperationRepository
import java.time.OffsetDateTime

class OperationRepository(
    private val api: OperationApi
) : IOperationRepository {

    override suspend fun getOperations(
        bankAccountSourceId: Int?,
        state: StateOperationsGet?,
        dateFrom: OffsetDateTime?,
        dateTo: OffsetDateTime?
    ): NetworkResult<OperationsGet200Response> {
        return safeApiCall {
            api.operationsGet(
                bankAccountSourceId,
                state,
                dateFrom,
                dateTo
            )
        }
    }

    override suspend fun getOperationById(id: Int): NetworkResult<Operation> {
        return safeApiCall { api.operationsIdGet(id) }
    }

    override suspend fun createOperation(
        request: OperationsPostRequest
    ): NetworkResult<Operation> {
        return safeApiCall { api.operationsPost(request) }
    }

    override suspend fun cancelOperation(
        id: Int,
        request: OperationsIdCancelPostRequest?
    ): NetworkResult<OperationsIdCancelPost201Response> {
        return safeApiCall { api.operationsIdCancelPost(id, request) }
    }

    override suspend fun changeOperationState(
        id: Int,
        request: OperationsIdStatePatchRequest
    ): NetworkResult<Operation> {
        return safeApiCall { api.operationsIdStatePatch(id, request) }
    }
}
