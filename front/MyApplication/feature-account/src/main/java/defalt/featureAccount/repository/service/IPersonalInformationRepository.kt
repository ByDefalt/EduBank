package defalt.featureAccount.repository.service

import defalt.core.api.account.model.PersonalInformation
import defalt.core.api.account.model.PersonalInformationRegister
import defalt.core.utils.NetworkResult

interface IPersonalInformationRepository {
    suspend fun getAllPersonalInformation(): NetworkResult<List<PersonalInformation>>
    suspend fun getPersonalInformationById(id: Int): NetworkResult<PersonalInformation>
    suspend fun createPersonalInformation(personalInformationRegister: PersonalInformationRegister): NetworkResult<PersonalInformation>
}
