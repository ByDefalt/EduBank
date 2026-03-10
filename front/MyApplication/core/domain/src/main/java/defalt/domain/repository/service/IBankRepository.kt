package defalt.domain.repository.service

import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.bank.BankAccountCreateRequest
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.BankAccountParameter
import defalt.domain.entity.bank.BankAccountPivot
import defalt.domain.entity.bank.Type
import defalt.utils.NetworkResult

interface IBankRepository {

    // --- ADMIN : Comptes bancaires ---
    /** [ADMIN] Liste les comptes bancaires rattachés à un ID utilisateur */
    suspend fun adminGetBankAccountsByAccountId(accountId: String): NetworkResult<List<BankAccount>>

    /** [ADMIN] Crée un nouveau compte bancaire pour un utilisateur */
    suspend fun adminCreateBankAccount(accountId: String, request: BankAccountCreateRequest): NetworkResult<BankAccountDetail>

    /** [ADMIN] Liste l'intégralité des comptes bancaires du système */
    suspend fun adminGetAllBankAccounts(): NetworkResult<List<BankAccount>>

    /** [ADMIN] Supprime un compte bancaire par son ID */
    suspend fun adminDeleteBankAccount(id: String): NetworkResult<Unit>

    /** [ADMIN] Récupère les détails complets d'un compte bancaire */
    suspend fun adminGetBankAccountById(id: String): NetworkResult<BankAccountDetail>

    // --- ADMIN : Paramètres ---
    /** [ADMIN] Met à jour les paramètres (découvert, état) d'un compte bancaire */
    suspend fun adminUpdateBankAccountParameters(bankAccountId: String, parameter: BankAccountParameter): NetworkResult<Unit>

    // --- ADMIN : Types de compte ---
    /** [ADMIN] Liste tous les types de comptes disponibles */
    suspend fun adminGetAllTypes(): NetworkResult<List<Type>>

    /** [ADMIN] Récupère un type de compte par son ID */
    suspend fun adminGetTypeById(id: Int): NetworkResult<Type>

    /** [ADMIN] Crée un nouveau type de compte */
    suspend fun adminCreateType(type: Type): NetworkResult<Type>

    // --- ADMIN : Co-titulaires / Pivot ---
    /** [ADMIN] Ajoute un co-titulaire à un compte */
    suspend fun adminAddCoHolder(pivot: BankAccountPivot): NetworkResult<Unit>

    /** [ADMIN] Retire un co-titulaire spécifique d'un compte */
    suspend fun adminRemoveCoHolder(pivot: BankAccountPivot): NetworkResult<Unit>

    /** [ADMIN] Supprime tous les liens de co-titularité pour un compte bancaire donné */
    suspend fun adminRemoveAllCoHoldersByBankAccount(bankAccountId: String): NetworkResult<Unit>

    /** [ADMIN] Supprime tous les liens entre un utilisateur et ses comptes bancaires */
    suspend fun adminRemoveAllBankAccountsByAccount(accountId: String): NetworkResult<Unit>

    /** [ADMIN] Récupère la liste des liens pivots (co-titulaires) d'un compte bancaire */
    suspend fun adminGetCoHoldersByBankAccount(bankAccountId: String): NetworkResult<List<BankAccountPivot>>

    // --- CLIENT : Mes Comptes ---
    /** [CLIENT] Récupère les comptes bancaires de l'utilisateur connecté (filtrage par type optionnel) */
    suspend fun getMyBankAccounts(typeId: Int? = null): NetworkResult<List<BankAccount>>

    /** [CLIENT] Récupère les détails d'un compte appartenant à l'utilisateur */
    suspend fun getMyBankAccountById(id: String): NetworkResult<BankAccountDetail>

    /** [CLIENT] Récupère uniquement les IDs des co-titulaires pour un de ses comptes */
    suspend fun getMyBankAccountCoHolders(id: String): NetworkResult<List<String>>

    /** [CLIENT] Récupère tous les liens pivots associés à l'ID utilisateur */
    suspend fun getMyPivotsByAccountId(accountId: String): NetworkResult<List<BankAccountPivot>>
}
