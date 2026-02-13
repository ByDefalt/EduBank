package defalt.featureBank.repository.impl

import defalt.core.api.bank.model.Type
import defalt.core.api.bank.model.TypesIdPutRequest
import defalt.core.api.bank.model.TypesPostRequest
import defalt.core.api.bank.service.TypeApi
import defalt.core.utils.NetworkResult
import defalt.core.utils.safeApiCall
import defalt.featureBank.repository.service.ITypeRepository

class TypeRepository(
    private val api: TypeApi,
) : ITypeRepository {

    override suspend fun getTypes(): NetworkResult<List<Type>> {
        return safeApiCall { api.typesGet() }
    }

    override suspend fun getTypeById(id: Int): NetworkResult<Type> {
        return safeApiCall { api.typesIdGet(id) }
    }

    override suspend fun createType(request: TypesPostRequest): NetworkResult<Type> {
        return safeApiCall { api.typesPost(request) }
    }

    override suspend fun updateType(id: Int, request: TypesIdPutRequest): NetworkResult<Type> {
        return safeApiCall { api.typesIdPut(id, request) }
    }

    override suspend fun deleteType(id: Int): NetworkResult<Unit> {
        return safeApiCall { api.typesIdDelete(id) }
    }
}
