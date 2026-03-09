package defalt.featureBank.usecase

import defalt.domain.entity.account.PersonalInformation
import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.repository.service.IAccountRepository
import defalt.domain.repository.service.IBankRepository
import defalt.domain.session.Session
import defalt.testing.FakeLogger
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetHomeDataTest {

    private val bankRepository: IBankRepository = mockk()
    private val accountRepository: IAccountRepository = mockk()
    private val session = Session(token = "tok", accountId = "acc-001", role = "CUSTOMER")
    private val logger = FakeLogger()
    private lateinit var useCase: GetHomeData

    private val fakeBankAccount = BankAccount(id = "bank-001", parameterId = 1, typeId = 1, sold = 1000.0, iban = "FR76...")
    private val fakeBankAccountDetail = BankAccountDetail(id = "bank-001", sold = 1000.0, iban = "FR76...")
    private val fakeInfo = PersonalInformation(id = 1, firstname = "Alice", lastname = "Dupont", email = "alice@mail.fr")

    @Before fun setUp() { useCase = GetHomeData(bankRepository, accountRepository, session, logger) }

    @Test fun `retourne HomeData quand tous les appels reussissent`() = runTest {
        coEvery { bankRepository.getMyBankAccounts() } returns NetworkResult.Success(listOf(fakeBankAccount))
        coEvery { bankRepository.getMyBankAccountById("bank-001") } returns NetworkResult.Success(fakeBankAccountDetail)
        coEvery { accountRepository.getPersonalInformationByAccountId("acc-001") } returns NetworkResult.Success(fakeInfo)

        val result = useCase()

        assertTrue(result is NetworkResult.Success)
        val data = (result as NetworkResult.Success).data
        assertEquals(fakeBankAccountDetail, data.account)
        assertEquals(fakeInfo, data.personalInformation)
    }

    @Test fun `propage l erreur si getMyBankAccounts echoue`() = runTest {
        coEvery { bankRepository.getMyBankAccounts() } returns NetworkResult.Error(500, "Erreur serveur")

        val result = useCase()

        assertTrue(result is NetworkResult.Error)
        assertEquals("Erreur serveur", (result as NetworkResult.Error).message)
        coVerify(exactly = 0) { bankRepository.getMyBankAccountById(any()) }
        coVerify(exactly = 0) { accountRepository.getPersonalInformationByAccountId(any()) }
    }

    @Test fun `propage l erreur si getMyBankAccountById echoue`() = runTest {
        coEvery { bankRepository.getMyBankAccounts() } returns NetworkResult.Success(listOf(fakeBankAccount))
        coEvery { bankRepository.getMyBankAccountById("bank-001") } returns NetworkResult.Error(404, "Compte non trouve")

        val result = useCase()

        assertTrue(result is NetworkResult.Error)
        assertEquals("Compte non trouve", (result as NetworkResult.Error).message)
        coVerify(exactly = 0) { accountRepository.getPersonalInformationByAccountId(any()) }
    }

    @Test fun `propage l erreur si getPersonalInformation echoue`() = runTest {
        coEvery { bankRepository.getMyBankAccounts() } returns NetworkResult.Success(listOf(fakeBankAccount))
        coEvery { bankRepository.getMyBankAccountById("bank-001") } returns NetworkResult.Success(fakeBankAccountDetail)
        coEvery { accountRepository.getPersonalInformationByAccountId("acc-001") } returns NetworkResult.Error(404, "Info non trouvee")

        val result = useCase()

        assertTrue(result is NetworkResult.Error)
        assertEquals("Info non trouvee", (result as NetworkResult.Error).message)
    }

    @Test fun `propage l exception reseau`() = runTest {
        val ex = RuntimeException("no internet")
        coEvery { bankRepository.getMyBankAccounts() } returns NetworkResult.Exception(ex)

        val result = useCase()

        assertTrue(result is NetworkResult.Exception)
        assertEquals(ex, (result as NetworkResult.Exception).throwable)
    }
}
