package defalt.featureAccount.usecase

import defalt.domain.entity.account.PersonalInformation
import defalt.domain.repository.service.IAccountRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class UpdatePersonalInfoUseCase(
    private val repository: IAccountRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: Int, personalInformation: PersonalInformation): NetworkResult<PersonalInformation> {
        logger.debug("UpdatePersonalInfoUseCase")
        return repository.updatePersonalInformation(id, personalInformation)
    }
}
