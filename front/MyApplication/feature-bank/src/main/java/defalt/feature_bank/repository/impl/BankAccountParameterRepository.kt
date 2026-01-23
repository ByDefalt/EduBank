package defalt.feature_bank.repository.impl

import defalt.core.api.bank.model.BankAccountParameter
import defalt.core.api.bank.model.ParametersIdPutRequest
import defalt.core.api.bank.model.ParametersPostRequest
import defalt.core.api.bank.service.BankAccountParameterApi
import defalt.core.utils.NetworkResult
import defalt.core.utils.safeApiCall
import defalt.feature_bank.repository.service.IBankAccountParameterRepository

class BankAccountParameterRepository(
    private val api: BankAccountParameterApi
) : IBankAccountParameterRepository {

    override suspend fun getParameters(): NetworkResult<List<BankAccountParameter>> {
        return safeApiCall { api.parametersGet() }
    }

    override suspend fun getParameterById(id: Int): NetworkResult<BankAccountParameter> {
        return safeApiCall { api.parametersIdGet(id) }
    }

    override suspend fun createParameter(request: ParametersPostRequest): NetworkResult<BankAccountParameter> {
        return safeApiCall { api.parametersPost(request) }
    }

    override suspend fun updateParameter(id: Int, request: ParametersIdPutRequest): NetworkResult<BankAccountParameter> {
        return safeApiCall { api.parametersIdPut(id, request) }
    }

    override suspend fun deleteParameter(id: Int): NetworkResult<Unit> {
        return safeApiCall { api.parametersIdDelete(id) }
    }
}
