package defalt.feature_account.repository.impl

import defalt.core.api.account.service.PersonalInformationApi
import defalt.core.infrastructure.ApiClient
import defalt.feature_account.repository.service.PersonalInformationRepository

class PersonalInformationRepositoryImpl(
    private val api: PersonalInformationApi
) : PersonalInformationRepository {
}