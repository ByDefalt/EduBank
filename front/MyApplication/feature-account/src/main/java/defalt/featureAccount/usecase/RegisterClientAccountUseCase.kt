package defalt.featureAccount.usecase

import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountRegister
import defalt.domain.entity.account.PersonalInformationRegister
import defalt.domain.entity.account.RoleEnum
import defalt.domain.repository.service.IAccountRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class RegisterClientAccountUseCase(
    private val repository: IAccountRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firstname: String,
        lastname: String,
        address: String,
        phoneNumber: String,
    ): NetworkResult<Account> {
        logger.debug("RegisterClientAccountUseCase")
        return repository.createAccount(
            AccountRegister(
                role = RoleEnum.CUSTOMER,
                password = password,
                personalInfo = PersonalInformationRegister(
                    email = email,
                    firstname = firstname,
                    lastname = lastname,
                    address = address,
                    phoneNumber = phoneNumber,
                ),
            ),
        )
    }
}
