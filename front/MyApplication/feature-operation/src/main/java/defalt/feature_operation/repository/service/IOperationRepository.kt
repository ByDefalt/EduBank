package defalt.feature_operation.repository.service

import defalt.core.api.operation.model.*
import defalt.core.api.operation.service.OperationApi.StateOperationsGet
import defalt.core.utils.NetworkResult
import java.time.OffsetDateTime

interface IOperationRepository {

    suspend fun getOperations(
        bankAccountSourceId: Int? = null,
        state: StateOperationsGet? = null,
        dateFrom: OffsetDateTime? = null,
        dateTo: OffsetDateTime? = null
    ): NetworkResult<OperationsGet200Response>

    suspend fun getOperationById(id: Int): NetworkResult<Operation>

    suspend fun createOperation(
        request: OperationsPostRequest
    ): NetworkResult<Operation>

    suspend fun cancelOperation(
        id: Int,
        request: OperationsIdCancelPostRequest? = null
    ): NetworkResult<OperationsIdCancelPost201Response>

    suspend fun changeOperationState(
        id: Int,
        request: OperationsIdStatePatchRequest
    ): NetworkResult<Operation>
}
