package defalt.featureOperation.usecase

import defalt.domain.entity.operation.Beneficiary
import defalt.domain.repository.service.IOperationRepository
import defalt.domain.session.Session
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

// ── AddBeneficiary ───────────────────────────────────────────────────────────

class AddBeneficiaryUseCaseTest {

    private val repository: IOperationRepository = mockk()
    private val session = Session(token = "tok", accountId = "acc-001", role = "CUSTOMER")
    private val logger = FakeLogger()
    private lateinit var useCase: AddBeneficiary

    @Before fun setUp() { useCase = AddBeneficiary(repository, session, logger) }

    @Test fun `cree le beneficiaire avec les bonnes donnees`() = runTest {
        val slot = slot<Beneficiary>()
        val fakeBenef = Beneficiary(id = 1, accountSourceId = "acc-001", ibanTarget = "FR76...", name = "Alice")
        coEvery { repository.createBeneficiary(capture(slot)) } returns NetworkResult.Success(fakeBenef)

        val result = useCase("Alice", "FR76...")

        assertTrue(result is NetworkResult.Success)
        assertEquals("Alice", slot.captured.name)
        assertEquals("FR76...", slot.captured.ibanTarget)
        assertEquals("acc-001", slot.captured.accountSourceId)
        coVerify(exactly = 1) { repository.createBeneficiary(any()) }
    }

    @Test fun `propage l erreur du repository`() = runTest {
        coEvery { repository.createBeneficiary(any()) } returns NetworkResult.Error(400, "IBAN invalide")

        val result = useCase("Bob", "INVALID")

        assertTrue(result is NetworkResult.Error)
        assertEquals("IBAN invalide", (result as NetworkResult.Error).message)
    }

    @Test fun `propage l exception reseau`() = runTest {
        coEvery { repository.createBeneficiary(any()) } returns NetworkResult.Exception(RuntimeException("crash"))
        assertTrue(useCase("Bob", "FR76...") is NetworkResult.Exception)
    }
}

// ── EditBeneficiary ──────────────────────────────────────────────────────────

class EditBeneficiaryUseCaseTest {

    private val repository: IOperationRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: EditBeneficiary

    @Before fun setUp() { useCase = EditBeneficiary(repository, logger) }

    @Test fun `met a jour le beneficiaire avec les bonnes donnees`() = runTest {
        val slot = slot<Beneficiary>()
        val fakeBenef = Beneficiary(id = 1, accountSourceId = "acc-001", ibanTarget = "FR76...", name = "Alice")
        coEvery { repository.updateBeneficiary(1, capture(slot)) } returns NetworkResult.Success(fakeBenef)

        val result = useCase(1, "Alice Modifie", "FR76...", "acc-001")

        assertTrue(result is NetworkResult.Success)
        assertEquals("Alice Modifie", slot.captured.name)
        assertEquals(1, slot.captured.id)
        assertEquals("acc-001", slot.captured.accountSourceId)
    }

    @Test fun `propage l erreur`() = runTest {
        coEvery { repository.updateBeneficiary(any(), any()) } returns NetworkResult.Error(404, "Non trouve")
        assertTrue(useCase(1, "Alice", "FR76...", "acc-001") is NetworkResult.Error)
    }

    @Test fun `propage l exception`() = runTest {
        coEvery { repository.updateBeneficiary(any(), any()) } returns NetworkResult.Exception(RuntimeException())
        assertTrue(useCase(1, "Alice", "FR76...", "acc-001") is NetworkResult.Exception)
    }
}

// ── DeleteBeneficiary ────────────────────────────────────────────────────────

class DeleteBeneficiaryUseCaseTest {

    private val repository: IOperationRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: DeleteBeneficiary

    @Before fun setUp() { useCase = DeleteBeneficiary(repository, logger) }

    @Test fun `supprime le beneficiaire avec succes`() = runTest {
        coEvery { repository.deleteBeneficiary(1) } returns NetworkResult.Success(Unit)

        val result = useCase(1)

        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { repository.deleteBeneficiary(1) }
    }

    @Test fun `propage l erreur`() = runTest {
        coEvery { repository.deleteBeneficiary(1) } returns NetworkResult.Error(404, "Non trouve")
        val result = useCase(1)
        assertTrue(result is NetworkResult.Error)
        assertEquals(404, (result as NetworkResult.Error).code)
    }

    @Test fun `propage l exception`() = runTest {
        coEvery { repository.deleteBeneficiary(1) } returns NetworkResult.Exception(RuntimeException("crash"))
        assertTrue(useCase(1) is NetworkResult.Exception)
    }
}

// ── GetMyBeneficiaries ───────────────────────────────────────────────────────

class GetMyBeneficiariesUseCaseTest {

    private val repository: IOperationRepository = mockk()
    private val session = Session(token = "tok", accountId = "acc-001", role = "CUSTOMER")
    private val logger = FakeLogger()
    private lateinit var useCase: GetMyBeneficiaries

    @Before fun setUp() { useCase = GetMyBeneficiaries(repository, session, logger) }

    @Test fun `retourne la liste des beneficiaires`() = runTest {
        val benefs = listOf(
            Beneficiary(id = 1, accountSourceId = "acc-001", ibanTarget = "FR76...1", name = "Alice"),
            Beneficiary(id = 2, accountSourceId = "acc-001", ibanTarget = "FR76...2", name = "Bob"),
        )
        coEvery { repository.getBeneficiariesByAccountId("acc-001") } returns NetworkResult.Success(benefs)

        val result = useCase()

        assertTrue(result is NetworkResult.Success)
        assertEquals(2, (result as NetworkResult.Success).data.size)
        coVerify(exactly = 1) { repository.getBeneficiariesByAccountId("acc-001") }
    }

    @Test fun `retourne liste vide`() = runTest {
        coEvery { repository.getBeneficiariesByAccountId("acc-001") } returns NetworkResult.Success(emptyList())
        val result = useCase()
        assertTrue((result as NetworkResult.Success).data.isEmpty())
    }

    @Test fun `propage l erreur`() = runTest {
        coEvery { repository.getBeneficiariesByAccountId("acc-001") } returns NetworkResult.Error(500, "Erreur")
        assertTrue(useCase() is NetworkResult.Error)
    }
}

