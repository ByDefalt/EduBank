package defalt.feature_operation.repository.impl

import defalt.core.api.operation.model.BeneficiariesIdPutRequest
import defalt.core.api.operation.model.BeneficiariesPostRequest
import defalt.core.api.operation.model.Beneficiary
import defalt.core.api.operation.service.BeneficiaryApi
import defalt.core.utils.NetworkResult
import defalt.core.utils.safeApiCall
import defalt.feature_operation.repository.service.IBeneficiaryRepository

class BeneficiaryRepository(
    private val api: BeneficiaryApi
) : IBeneficiaryRepository {

    override suspend fun getBeneficiaries(
        accountSourceId: Int?
    ): NetworkResult<List<Beneficiary>> {
        return safeApiCall { api.beneficiariesGet(accountSourceId) }
    }

    override suspend fun getBeneficiaryById(id: Int): NetworkResult<Beneficiary> {
        return safeApiCall { api.beneficiariesIdGet(id) }
    }

    override suspend fun createBeneficiary(
        request: BeneficiariesPostRequest
    ): NetworkResult<Unit> {
        return safeApiCall { api.beneficiariesPost(request) }
    }

    override suspend fun updateBeneficiary(
        id: Int,
        request: BeneficiariesIdPutRequest
    ): NetworkResult<Beneficiary> {
        return safeApiCall { api.beneficiariesIdPut(id, request) }
    }

    override suspend fun deleteBeneficiary(id: Int): NetworkResult<Unit> {
        return safeApiCall { api.beneficiariesIdDelete(id) }
    }
}
