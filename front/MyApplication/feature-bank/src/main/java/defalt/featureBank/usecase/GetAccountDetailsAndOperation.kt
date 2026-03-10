package defalt.featureBank.usecase

import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.operation.Operation
import defalt.domain.repository.service.IBankRepository
import defalt.domain.repository.service.IOperationRepository
import defalt.domain.session.Session
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

data class AccountDetailsAndOperation(
    val accountDetail: BankAccountDetail,
    val operations: List<Operation>,
)

class GetAccountDetailsAndOperation(
    private val bankRepository: IBankRepository,
    private val operationRepository: IOperationRepository,
    private val session: Session,
    private val logger: Logger,
) {
    suspend operator fun invoke(bankAccountId: String): NetworkResult<AccountDetailsAndOperation> {
        val detailResult = bankRepository.getMyBankAccountById(bankAccountId)
        if (detailResult is NetworkResult.Error) {
            logger.error("GetAccountDetailsAndOperation - Erreur détail compte ${session.accountId} : ${detailResult.message}")
            return NetworkResult.Error(detailResult.code, detailResult.message)
        }
        if (detailResult is NetworkResult.Exception) {
            logger.error("GetAccountDetailsAndOperation - Exception détail compte : ${detailResult.throwable.message}", detailResult.throwable)
            return NetworkResult.Exception(detailResult.throwable)
        }

        val detail = (detailResult as NetworkResult.Success).data

        val operationsResult = operationRepository.getOperations(
            accountId = session.accountId,
        )

        val operations = when (operationsResult) {
            is NetworkResult.Success -> operationsResult.data
            is NetworkResult.Error -> {
                logger.warn("GetAccountDetailsAndOperation - Erreur opérations : ${operationsResult.message}")
                emptyList()
            }
            is NetworkResult.Exception -> {
                logger.warn("GetAccountDetailsAndOperation - Exception opérations : ${operationsResult.throwable.message}")
                emptyList()
            }
        }

        return NetworkResult.Success(
            AccountDetailsAndOperation(
                accountDetail = detail,
                operations = operations,
            ),
        )
    }
}
