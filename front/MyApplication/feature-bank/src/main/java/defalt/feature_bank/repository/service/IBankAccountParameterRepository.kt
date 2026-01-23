package defalt.feature_bank.repository.service

import defalt.core.api.bank.model.BankAccountParameter
import defalt.core.api.bank.model.ParametersIdPutRequest
import defalt.core.api.bank.model.ParametersPostRequest
import defalt.core.utils.NetworkResult

interface IBankAccountParameterRepository {

    suspend fun getParameters(): NetworkResult<List<BankAccountParameter>>

    suspend fun getParameterById(id: Int): NetworkResult<BankAccountParameter>

    suspend fun createParameter(request: ParametersPostRequest): NetworkResult<BankAccountParameter>

    suspend fun updateParameter(id: Int, request: ParametersIdPutRequest): NetworkResult<BankAccountParameter>

    suspend fun deleteParameter(id: Int): NetworkResult<Unit>
}
