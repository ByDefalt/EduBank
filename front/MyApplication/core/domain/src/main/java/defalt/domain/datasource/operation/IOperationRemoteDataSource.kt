package defalt.domain.datasource.operation

import defalt.domain.entity.operation.Beneficiary
import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import defalt.utils.NetworkResult
import java.time.OffsetDateTime

interface IOperationRemoteDataSource {

    // --- OPÉRATIONS (OperationApi) ---

    /**
     * Récupère la liste des opérations avec filtres.
     * Si [accountId] est spécifié, récupère les opérations d'un compte précis.
     * Sinon, récupère la liste globale (Admin/Toutes).
     * Correspond à GET operations/account/{accountId} ou GET operations
     */
    suspend fun getOperations(
        accountId: String? = null,
        state: OperationState? = null,
        dateFrom: OffsetDateTime? = null,
        dateTo: OffsetDateTime? = null,
    ): NetworkResult<List<Operation>>

    /**
     * Récupère les détails d'une opération par son ID.
     * Correspond à GET operations/{id}
     */
    suspend fun getOperationById(id: Int): NetworkResult<Operation>

    /**
     * Crée une nouvelle opération (virement).
     * Correspond à POST operations
     */
    suspend fun createOperation(operation: Operation): NetworkResult<Operation>

    /**
     * Annule une opération en cours.
     * Correspond à PUT operations/{id}/cancel
     */
    suspend fun cancelOperation(id: Int): NetworkResult<Operation>

    /**
     * Met à jour l'état d'une opération (Admin).
     * Correspond à PATCH operations/{id}/state
     */
    suspend fun updateOperationState(id: Int, state: OperationState): NetworkResult<Operation>

    // --- BÉNÉFICIAIRES (BeneficiaryApi) ---

    /**
     * Récupère la liste globale de tous les bénéficiaires.
     * Correspond à GET beneficiaries
     */
    suspend fun getAllBeneficiaries(): NetworkResult<List<Beneficiary>>

    /**
     * Récupère les bénéficiaires associés à un compte source spécifique.
     * Correspond à GET beneficiaries/{accountId}
     */
    suspend fun getBeneficiariesByAccountId(accountId: String): NetworkResult<List<Beneficiary>>

    /**
     * Ajoute un nouveau bénéficiaire.
     * Correspond à POST beneficiaries
     */
    suspend fun createBeneficiary(beneficiary: Beneficiary): NetworkResult<Beneficiary>

    /**
     * Met à jour les informations d'un bénéficiaire existant.
     * Correspond à PUT beneficiaries/{id}
     */
    suspend fun updateBeneficiary(id: Int, beneficiary: Beneficiary): NetworkResult<Beneficiary>

    /**
     * Supprime un bénéficiaire.
     * Correspond à DELETE beneficiaries/{id}
     */
    suspend fun deleteBeneficiary(id: Int): NetworkResult<Unit>
}
