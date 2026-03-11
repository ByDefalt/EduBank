package defalt.featureOperation.usecase

import defalt.domain.entity.account.RoleEnum
import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
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
import java.time.OffsetDateTime

class CreateTransferUseCaseTest {

    private val repository: IOperationRepository = mockk()
    private val session = Session(token = "tok", accountId = "acc-001", role = RoleEnum.CUSTOMER)
    private val logger = FakeLogger()
    private lateinit var useCase: CreateTransfer

    @Before fun setUp() { useCase = CreateTransfer(repository, session, logger) }

    @Test fun `crée le virement avec les bonnes données`() = runTest {
        val slot = slot<Operation>()
        val fakeOp = Operation(1, "acc-001", "Virement", OperationState.PENDING, "FR76...", 100.0, OffsetDateTime.now())
        coEvery { repository.createOperation(capture(slot)) } returns NetworkResult.Success(fakeOp)

        val result = useCase("FR76...", 100.0, "Virement")

        assertTrue(result is NetworkResult.Success)
        assertEquals("FR76...", slot.captured.ibanTarget)
        assertEquals(100.0, slot.captured.amount, 0.0)
        assertEquals("Virement", slot.captured.label)
        assertEquals("acc-001", slot.captured.accountSourceId)
        assertEquals(OperationState.PENDING, slot.captured.state)
    }

    @Test fun `propage l erreur du repository`() = runTest {
        coEvery { repository.createOperation(any()) } returns NetworkResult.Error(400, "IBAN invalide")

        val result = useCase("INVALID", 50.0, "Test")

        assertTrue(result is NetworkResult.Error)
        assertEquals("IBAN invalide", (result as NetworkResult.Error).message)
    }

    @Test fun `propage l exception réseau`() = runTest {
        val ex = RuntimeException("timeout")
        coEvery { repository.createOperation(any()) } returns NetworkResult.Exception(ex)

        val result = useCase("FR76...", 10.0, "Test")

        assertTrue(result is NetworkResult.Exception)
        assertEquals(ex, (result as NetworkResult.Exception).throwable)
    }

    @Test fun `utilise l accountId de la session`() = runTest {
        val slot = slot<Operation>()
        coEvery { repository.createOperation(capture(slot)) } returns NetworkResult.Success(
            Operation(1, "acc-001", "L", OperationState.PENDING, "FR76", 1.0, OffsetDateTime.now()),
        )

        useCase("FR76", 1.0, "L")

        assertEquals("acc-001", slot.captured.accountSourceId)
        coVerify(exactly = 1) { repository.createOperation(any()) }
    }
}
