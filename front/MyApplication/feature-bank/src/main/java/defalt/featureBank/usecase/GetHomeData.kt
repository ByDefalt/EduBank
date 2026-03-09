package defalt.featureBank.usecase

import defalt.domain.repository.service.IAccountRepository
import defalt.domain.repository.service.IBankRepository
import defalt.domain.session.Session
import defalt.featureBank.viewModel.HomeData
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class GetHomeData(
    private val bankRepository: IBankRepository,
    private val accountRepository: IAccountRepository,
    private val session: Session,
    private val logger: Logger,
) {
    suspend operator fun invoke(): NetworkResult<HomeData> {
        logger.debug("GetHomeData")

        val listAccount = bankRepository.getMyBankAccounts()
        if (listAccount is NetworkResult.Error) return NetworkResult.Error(listAccount.code, listAccount.message)
        if (listAccount is NetworkResult.Exception) return NetworkResult.Exception(listAccount.throwable)
        listAccount as NetworkResult.Success

        val bankAccount = bankRepository.getMyBankAccountById(listAccount.data.first().id!!)
        if (bankAccount is NetworkResult.Error) return NetworkResult.Error(bankAccount.code, bankAccount.message)
        if (bankAccount is NetworkResult.Exception) return NetworkResult.Exception(bankAccount.throwable)
        bankAccount as NetworkResult.Success

        val personalInfo = accountRepository.getPersonalInformationByAccountId(session.accountId!!)
        if (personalInfo is NetworkResult.Error) return NetworkResult.Error(personalInfo.code, personalInfo.message)
        if (personalInfo is NetworkResult.Exception) return NetworkResult.Exception(personalInfo.throwable)
        personalInfo as NetworkResult.Success

        return NetworkResult.Success(
            HomeData(
                account = bankAccount.data,
                personalInformation = personalInfo.data,
            ),
        )
    }
}
