package defalt.domain.repository.impl

import defalt.domain.datasource.operation.IOperationRemoteDataSource
import defalt.domain.entity.operation.Beneficiary
import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.OffsetDateTime

class OperationRepositoryTest {

    private val dataSource: IOperationRemoteDataSource = mockk()
    private lateinit var repository: OperationRepository

    // Utilise une date fixe pour éviter le flakiness des tests
    private val now: OffsetDateTime = OffsetDateTime.parse("2025-01-01T12:00:00Z")
    private val fakeOp = Operation(
        id = 1,
        accountSourceId = "acc-001",
        label = "Virement",
        state = OperationState.PENDING,
        ibanTarget = "FR76...",
        amount = 100.0,
        date = now,
    )
    private val fakeBen = Beneficiary(accountSourceId = "acc-001", ibanTarget = "FR76...", name = "Alice", id = 1)

    @Before fun setUp() { repository = OperationRepository(dataSource) }

    // ── getOperations ─────────────────────────────────────────────────────────

    @Test fun `getOperations delegue tous les parametres`() = runTest {
        coEvery { dataSource.getOperations("acc-001", OperationState.PENDING, null, null) } returns
            NetworkResult.Success(listOf(fakeOp))
        val result = repository.getOperations("acc-001", OperationState.PENDING, null, null)
        assertTrue(result is NetworkResult.Success)
        val data = (result as NetworkResult.Success).data
        assertEquals(1, data.size)
        assertEquals(fakeOp.id, data[0].id)
        coVerify(exactly = 1) { dataSource.getOperations("acc-001", OperationState.PENDING, null, null) }
    }

    @Test fun `getOperations avec parametres null`() = runTest {
        coEvery { dataSource.getOperations(null, null, null, null) } returns NetworkResult.Success(emptyList())
        assertTrue((repository.getOperations(null, null, null, null) as NetworkResult.Success).data.isEmpty())
    }

    @Test fun `getOperations propage Error`() = runTest {
        coEvery { dataSource.getOperations(any(), any(), any(), any()) } returns NetworkResult.Error(401, "unauthorized")
        assertTrue(repository.getOperations(null, null, null, null) is NetworkResult.Error)
    }

    // ── getOperationById ──────────────────────────────────────────────────────

    @Test fun `getOperationById delegue l id`() = runTest {
        coEvery { dataSource.getOperationById(1) } returns NetworkResult.Success(fakeOp)
        val result = repository.getOperationById(1)
        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.id)
        coVerify(exactly = 1) { dataSource.getOperationById(1) }
    }

    @Test fun `getOperationById propage Error 404`() = runTest {
        coEvery { dataSource.getOperationById(any()) } returns NetworkResult.Error(404, "not found")
        assertTrue(repository.getOperationById(1) is NetworkResult.Error)
    }

    // ── createOperation ───────────────────────────────────────────────────────

    @Test fun `createOperation delegue l operation`() = runTest {
        coEvery { dataSource.createOperation(fakeOp) } returns NetworkResult.Success(fakeOp)
        val result = repository.createOperation(fakeOp)
        assertTrue(result is NetworkResult.Success)
        assertEquals("Virement", (result as NetworkResult.Success).data.label)
        assertEquals(fakeOp.id, result.data.id)
        coVerify(exactly = 1) { dataSource.createOperation(fakeOp) }
    }

    @Test fun `createOperation propage Exception`() = runTest {
        coEvery { dataSource.createOperation(any()) } returns NetworkResult.Exception(RuntimeException())
        assertTrue(repository.createOperation(fakeOp) is NetworkResult.Exception)
    }

    // ── cancelOperation ───────────────────────────────────────────────────────

    @Test fun `cancelOperation delegue l id`() = runTest {
        val cancelled = fakeOp.copy(state = OperationState.CANCELLED)
        coEvery { dataSource.cancelOperation(1) } returns NetworkResult.Success(cancelled)
        val result = repository.cancelOperation(1)
        assertTrue(result is NetworkResult.Success)
        assertEquals(OperationState.CANCELLED, (result as NetworkResult.Success).data.state)
        coVerify(exactly = 1) { dataSource.cancelOperation(1) }
    }

    @Test fun `cancelOperation propage Error 400`() = runTest {
        coEvery { dataSource.cancelOperation(any()) } returns NetworkResult.Error(400, "cannot cancel")
        assertTrue(repository.cancelOperation(1) is NetworkResult.Error)
    }

    // ── updateOperationState ──────────────────────────────────────────────────

    @Test fun `updateOperationState delegue id et state`() = runTest {
        coEvery { dataSource.updateOperationState(1, OperationState.COMPLETED) } returns NetworkResult.Success(fakeOp)
        val result = repository.updateOperationState(1, OperationState.COMPLETED)
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { dataSource.updateOperationState(1, OperationState.COMPLETED) }
    }

    // ── getAllBeneficiaries ────────────────────────────────────────────────────

    @Test fun `getAllBeneficiaries delegue`() = runTest {
        coEvery { dataSource.getAllBeneficiaries() } returns NetworkResult.Success(listOf(fakeBen))
        val result = repository.getAllBeneficiaries()
        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
        assertEquals("Alice", result.data[0].name)
        coVerify(exactly = 1) { dataSource.getAllBeneficiaries() }
    }

    @Test fun `getAllBeneficiaries propage Error`() = runTest {
        coEvery { dataSource.getAllBeneficiaries() } returns NetworkResult.Error(500, "err")
        assertTrue(repository.getAllBeneficiaries() is NetworkResult.Error)
    }

    // ── getBeneficiariesByAccountId ────────────────────────────────────────────

    @Test fun `getBeneficiariesByAccountId delegue l accountId`() = runTest {
        coEvery { dataSource.getBeneficiariesByAccountId("acc-001") } returns NetworkResult.Success(listOf(fakeBen))
        val result = repository.getBeneficiariesByAccountId("acc-001")
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { dataSource.getBeneficiariesByAccountId("acc-001") }
    }

    // ── createBeneficiary ─────────────────────────────────────────────────────

    @Test fun `createBeneficiary delegue`() = runTest {
        coEvery { dataSource.createBeneficiary(fakeBen) } returns NetworkResult.Success(fakeBen)
        val result = repository.createBeneficiary(fakeBen)
        assertTrue(result is NetworkResult.Success)
        assertEquals("Alice", (result as NetworkResult.Success).data.name)
        coVerify(exactly = 1) { dataSource.createBeneficiary(fakeBen) }
    }

    @Test fun `createBeneficiary propage Error`() = runTest {
        coEvery { dataSource.createBeneficiary(any()) } returns NetworkResult.Error(400, "invalid")
        assertTrue(repository.createBeneficiary(fakeBen) is NetworkResult.Error)
    }

    // ── updateBeneficiary ─────────────────────────────────────────────────----

    @Test fun `updateBeneficiary delegue id et beneficiary`() = runTest {
        val updated = fakeBen.copy(name = "Alice Modifie")
        coEvery { dataSource.updateBeneficiary(1, updated) } returns NetworkResult.Success(updated)
        val result = repository.updateBeneficiary(1, updated)
        assertTrue(result is NetworkResult.Success)
        assertEquals("Alice Modifie", (result as NetworkResult.Success).data.name)
        coVerify(exactly = 1) { dataSource.updateBeneficiary(1, updated) }
    }

    // ── deleteBeneficiary ─────────────────────────────────────────────────----

    @Test fun `deleteBeneficiary delegue l id`() = runTest {
        coEvery { dataSource.deleteBeneficiary(1) } returns NetworkResult.Success(Unit)
        val result = repository.deleteBeneficiary(1)
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { dataSource.deleteBeneficiary(1) }
    }

    @Test fun `deleteBeneficiary propage Error 404`() = runTest {
        coEvery { dataSource.deleteBeneficiary(any()) } returns NetworkResult.Error(404, "not found")
        assertTrue(repository.deleteBeneficiary(1) is NetworkResult.Error)
    }
}
