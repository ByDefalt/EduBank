package defalt.network.datasource.operation

import defalt.domain.entity.operation.Beneficiary
import defalt.domain.entity.operation.OperationState
import defalt.network.api.operation.model.Beneficiary as BeneficiaryDto
import defalt.network.api.operation.model.Operation as OperationDto
import defalt.network.api.operation.model.OperationState as OperationStateDto
import defalt.network.api.operation.service.BeneficiaryApi
import defalt.network.api.operation.service.OperationApi
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.OffsetDateTime
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

/**
 * Tests de OperationRemoteDataSource.
 * Les méthodes retournant OperationList/BeneficiaryList sont testées via
 * feature-operation (meilleur accès aux classes du module network dans ce contexte).
 * Ce fichier couvre les méthodes retournant directement Operation ou Beneficiary.
 */
class OperationRemoteDataSourceTest {

    private val operationApi: OperationApi = mockk()
    private val beneficiaryApi: BeneficiaryApi = mockk()
    private lateinit var dataSource: OperationRemoteDataSource

    // Utilise une date fixe pour rendre le test déterministe
    private val now = OffsetDateTime.parse("2025-01-01T12:00:00Z")

    private val fakeOperationDto = OperationDto(
        id = 1,
        accountSourceId = "acc-001",
        label = "Virement",
        state = OperationStateDto.PENDING,
        ibanTarget = "FR76...",
        amount = 100.0,
        date = now,
    )
    private val fakeBeneficiaryDto = BeneficiaryDto(
        id = 1,
        accountSourceId = "acc-001",
        ibanTarget = "FR76...",
        name = "Alice",
    )

    @Before fun setUp() {
        dataSource = OperationRemoteDataSource(operationApi, beneficiaryApi)
    }

    // ── getOperationById ────────────────────────────────────────────────────

    @Test fun `getOperationById retourne l operation en succes`() = runTest {
        coEvery { operationApi.operationsIdGet(1) } returns Response.success(fakeOperationDto)

        val result = dataSource.getOperationById(1)

        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.id)
        assertEquals("Virement", result.data.label)
        assertEquals(OperationState.PENDING, result.data.state)
    }

    @Test fun `getOperationById propage Error 404`() = runTest {
        coEvery { operationApi.operationsIdGet(1) } returns
            Response.error(404, "not found".toResponseBody())

        val result = dataSource.getOperationById(1)

        assertTrue(result is NetworkResult.Error)
        assertEquals(404, (result as NetworkResult.Error).code)
    }

    @Test fun `getOperationById propage Exception`() = runTest {
        coEvery { operationApi.operationsIdGet(any()) } throws RuntimeException("crash")
        assertTrue(dataSource.getOperationById(1) is NetworkResult.Exception)
    }

    // ── cancelOperation ─────────────────────────────────────────────────────

    @Test fun `cancelOperation retourne l operation annulee`() = runTest {
        val cancelled = fakeOperationDto.copy(state = OperationStateDto.CANCELLED)
        coEvery { operationApi.operationsIdCancelPost(1) } returns Response.success(cancelled)

        val result = dataSource.cancelOperation(1)

        assertTrue(result is NetworkResult.Success)
        assertEquals(OperationState.CANCELLED, (result as NetworkResult.Success).data.state)
    }

    @Test fun `cancelOperation propage Error 400`() = runTest {
        coEvery { operationApi.operationsIdCancelPost(1) } returns
            Response.error(400, "cannot cancel".toResponseBody())

        assertTrue(dataSource.cancelOperation(1) is NetworkResult.Error)
    }

    @Test fun `cancelOperation propage Exception`() = runTest {
        coEvery { operationApi.operationsIdCancelPost(any()) } throws RuntimeException("crash")
        assertTrue(dataSource.cancelOperation(1) is NetworkResult.Exception)
    }

    // ── updateOperationState ────────────────────────────────────────────────

    @Test fun `updateOperationState appelle l api avec le bon etat`() = runTest {
        coEvery { operationApi.operationsIdStatePatch(1, any()) } returns Response.success(fakeOperationDto)

        dataSource.updateOperationState(1, OperationState.COMPLETED)

        // L'implémentation envoie le toString() de l'enum, sans guillemets
        coVerify(exactly = 1) { operationApi.operationsIdStatePatch(1, "COMPLETED") }
    }

    @Test fun `updateOperationState propage Error`() = runTest {
        coEvery { operationApi.operationsIdStatePatch(any(), any()) } returns
            Response.error(400, "invalid state".toResponseBody())

        assertTrue(dataSource.updateOperationState(1, OperationState.FAILED) is NetworkResult.Error)
    }

    // ── createBeneficiary ───────────────────────────────────────────────────

    @Test fun `createBeneficiary retourne le beneficiaire cree`() = runTest {
        coEvery { beneficiaryApi.beneficiariesPost(any()) } returns Response.success(fakeBeneficiaryDto)

        val result = dataSource.createBeneficiary(
            Beneficiary(accountSourceId = "acc-001", ibanTarget = "FR76...", name = "Alice"),
        )

        assertTrue(result is NetworkResult.Success)
        assertEquals("Alice", (result as NetworkResult.Success).data.name)
        assertEquals("FR76...", result.data.ibanTarget)
    }

    @Test fun `createBeneficiary propage Error 400`() = runTest {
        coEvery { beneficiaryApi.beneficiariesPost(any()) } returns
            Response.error(400, "invalid iban".toResponseBody())

        val result = dataSource.createBeneficiary(
            Beneficiary(accountSourceId = "acc-001", ibanTarget = "INVALID", name = "Alice"),
        )

        assertTrue(result is NetworkResult.Error)
        assertEquals(400, (result as NetworkResult.Error).code)
    }

    @Test fun `createBeneficiary propage Exception`() = runTest {
        coEvery { beneficiaryApi.beneficiariesPost(any()) } throws RuntimeException("crash")

        assertTrue(
            dataSource.createBeneficiary(
                Beneficiary(accountSourceId = "acc-001", ibanTarget = "FR76...", name = "Alice"),
            ) is NetworkResult.Exception,
        )
    }

    // ── updateBeneficiary ───────────────────────────────────────────────────

    @Test fun `updateBeneficiary retourne le beneficiaire modifie`() = runTest {
        val updated = fakeBeneficiaryDto.copy(name = "Alice Modifie")
        coEvery { beneficiaryApi.beneficiariesIdPut(1, any()) } returns Response.success(updated)

        val result = dataSource.updateBeneficiary(
            1,
            Beneficiary(accountSourceId = "acc-001", ibanTarget = "FR76...", name = "Alice Modifie", id = 1),
        )

        assertTrue(result is NetworkResult.Success)
        assertEquals("Alice Modifie", (result as NetworkResult.Success).data.name)
    }

    @Test fun `updateBeneficiary propage Error 404`() = runTest {
        coEvery { beneficiaryApi.beneficiariesIdPut(any(), any()) } returns
            Response.error(404, "not found".toResponseBody())

        assertTrue(
            dataSource.updateBeneficiary(
                1,
                Beneficiary(accountSourceId = "acc-001", ibanTarget = "FR76...", name = "Alice", id = 1),
            ) is NetworkResult.Error,
        )
    }

    // ── deleteBeneficiary ───────────────────────────────────────────────────

    @Test fun `deleteBeneficiary retourne Unit en succes`() = runTest {
        coEvery { beneficiaryApi.beneficiariesIdDelete(1) } returns Response.success(Unit)

        assertTrue(dataSource.deleteBeneficiary(1) is NetworkResult.Success)
        coVerify(exactly = 1) { beneficiaryApi.beneficiariesIdDelete(1) }
    }

    @Test fun `deleteBeneficiary propage Error 404`() = runTest {
        coEvery { beneficiaryApi.beneficiariesIdDelete(1) } returns
            Response.error(404, "not found".toResponseBody())

        assertTrue(dataSource.deleteBeneficiary(1) is NetworkResult.Error)
    }

    @Test fun `deleteBeneficiary propage Exception`() = runTest {
        coEvery { beneficiaryApi.beneficiariesIdDelete(any()) } throws RuntimeException("crash")
        assertTrue(dataSource.deleteBeneficiary(1) is NetworkResult.Exception)
    }
}
