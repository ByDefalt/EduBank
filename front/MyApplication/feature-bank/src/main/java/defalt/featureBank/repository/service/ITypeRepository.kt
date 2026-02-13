package defalt.featureBank.repository.service

import defalt.core.api.bank.model.Type
import defalt.core.api.bank.model.TypesIdPutRequest
import defalt.core.api.bank.model.TypesPostRequest
import defalt.core.utils.NetworkResult

interface ITypeRepository {

    suspend fun getTypes(): NetworkResult<List<Type>>

    suspend fun getTypeById(id: Int): NetworkResult<Type>

    suspend fun createType(request: TypesPostRequest): NetworkResult<Type>

    suspend fun updateType(id: Int, request: TypesIdPutRequest): NetworkResult<Type>

    suspend fun deleteType(id: Int): NetworkResult<Unit>
}
