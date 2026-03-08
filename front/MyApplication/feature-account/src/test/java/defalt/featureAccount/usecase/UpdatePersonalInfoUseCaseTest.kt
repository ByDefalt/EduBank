package defalt.featureAccount.usecase

import defalt.domain.entity.account.PersonalInformation
import defalt.domain.repository.service.IAccountRepository
import defalt.testing.FakeLogger
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdatePersonalInfoUseCaseTest {

    private val repository: IAccountRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: UpdatePersonalInfoUseCase

    private val fakeInfo = PersonalInformation(id = 1, firstname = "Alice", lastname = "Dupont", email = "alice@mail.fr")

    @Before fun setUp() { useCase = UpdatePersonalInfoUseCase(repository, logger) }

    @Test fun `met a jour les informations et retourne les nouvelles infos`() = runTest {
        val slot = slot<PersonalInformation>()
        coEvery { repository.updatePersonalInformation(1, capture(slot)) } returns NetworkResult.Success(fakeInfo)

        val result = useCase(1, fakeInfo)

        assertTrue(result is NetworkResult.Success)
        assertEquals(fakeInfo, (result as NetworkResult.Success).data)
        assertEquals("Alice", slot.captured.firstname)
        coVerify(exactly = 1) { repository.updatePersonalInformation(1, any()) }
    }

    @Test fun `propage l erreur`() = runTest {
        coEvery { repository.updatePersonalInformation(1, any()) } returns NetworkResult.Error(404, "Info non trouvee")

        val result = useCase(1, fakeInfo)

        assertTrue(result is NetworkResult.Error)
        assertEquals(404, (result as NetworkResult.Error).code)
    }

    @Test fun `propage l exception`() = runTest {
        coEvery { repository.updatePersonalInformation(1, any()) } returns NetworkResult.Exception(RuntimeException("crash"))
        assertTrue(useCase(1, fakeInfo) is NetworkResult.Exception)
    }

    @Test fun `passe les bonnes donnees au repository`() = runTest {
        val slot = slot<PersonalInformation>()
        val updated = fakeInfo.copy(firstname = "Bob", email = "bob@mail.fr")
        coEvery { repository.updatePersonalInformation(1, capture(slot)) } returns NetworkResult.Success(updated)

        useCase(1, updated)

        assertEquals("Bob", slot.captured.firstname)
        assertEquals("bob@mail.fr", slot.captured.email)
        assertEquals(1, slot.captured.id)
    }
}

