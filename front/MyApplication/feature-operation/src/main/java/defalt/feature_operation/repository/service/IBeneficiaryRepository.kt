package defalt.feature_operation.repository.service

import defalt.core.api.operation.model.BeneficiariesIdPutRequest
import defalt.core.api.operation.model.BeneficiariesPostRequest
import defalt.core.api.operation.model.Beneficiary
import defalt.core.utils.NetworkResult

interface IBeneficiaryRepository {

    suspend fun getBeneficiaries(
        accountSourceId: Int? = null
    ): NetworkResult<List<Beneficiary>>

    suspend fun getBeneficiaryById(id: Int): NetworkResult<Beneficiary>

    suspend fun createBeneficiary(
        request: BeneficiariesPostRequest
    ): NetworkResult<Unit>

    suspend fun updateBeneficiary(
        id: Int,
        request: BeneficiariesIdPutRequest
    ): NetworkResult<Beneficiary>

    suspend fun deleteBeneficiary(id: Int): NetworkResult<Unit>
}
