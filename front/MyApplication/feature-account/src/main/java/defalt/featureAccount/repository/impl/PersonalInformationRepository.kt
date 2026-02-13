package defalt.featureAccount.repository.impl

import defalt.core.api.account.model.PersonalInformation
import defalt.core.api.account.model.PersonalInformationRegister
import defalt.core.api.account.service.PersonalInformationApi
import defalt.core.utils.NetworkResult
import defalt.core.utils.safeApiCall
import defalt.featureAccount.repository.service.IPersonalInformationRepository

class PersonalInformationRepository(
    private val api: PersonalInformationApi,
) : IPersonalInformationRepository {

    override suspend fun getAllPersonalInformation(): NetworkResult<List<PersonalInformation>> {
        return safeApiCall { api.personalInformationGet() }
    }

    override suspend fun getPersonalInformationById(id: Int): NetworkResult<PersonalInformation> {
        return safeApiCall { api.personalInformationIdGet(id) }
    }

    override suspend fun createPersonalInformation(personalInformationRegister: PersonalInformationRegister): NetworkResult<PersonalInformation> {
        return safeApiCall { api.personalInformationPost(personalInformationRegister) }
    }
}
