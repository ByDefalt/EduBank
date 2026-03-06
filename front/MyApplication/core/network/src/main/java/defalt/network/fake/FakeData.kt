package defalt.network.fake

import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountStateEnum
import defalt.domain.entity.account.PersonalInformation
import defalt.domain.entity.account.Role
import defalt.domain.entity.account.TokenRequest
import defalt.domain.entity.account.TokenResponse
import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.BankAccountParameter
import defalt.domain.entity.bank.State
import defalt.domain.entity.bank.Type
import defalt.domain.entity.offer.Offer
import defalt.domain.entity.operation.Beneficiary
import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 * Données cohérentes pour tous les fakes DataSource.
 *
 * Correspondances entre services :
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │  Account.id          ←→  Operation.accountSourceId                     │
 * │                      ←→  Beneficiary.accountSourceId                   │
 * │  Account.personalInfoId  ←→  PersonalInformation.id                    │
 * │  Account.roleId          ←→  Role.id                                   │
 * │  BankAccount.parameterId ←→  BankAccountParameter.id                   │
 * │  BankAccount.typeId      ←→  Type.id                                   │
 * │  BankAccount.iban        ←→  Operation.ibanTarget                      │
 * │                          ←→  Beneficiary.ibanTarget                    │
 * └─────────────────────────────────────────────────────────────────────────┘
 */
object FakeData {

    // ── IDs partagés ──────────────────────────────────────────────────────────

    // Comptes
    const val ACCOUNT_ID_ADMIN   = "acc-0001"
    const val ACCOUNT_ID_ALICE   = "acc-0002"
    const val ACCOUNT_ID_BOB     = "acc-0003"

    // Infos personnelles (Account.personalInfoId ←→ PersonalInformation.id)
    const val PERSONAL_INFO_ID_ADMIN = 1
    const val PERSONAL_INFO_ID_ALICE = 2
    const val PERSONAL_INFO_ID_BOB   = 3

    // Rôles (Account.roleId ←→ Role.id)
    const val ROLE_ID_ADMIN    = 1
    const val ROLE_ID_CUSTOMER = 2

    // Types de compte bancaire (BankAccount.typeId ←→ Type.id)
    const val BANK_TYPE_ID_COURANT  = 1
    const val BANK_TYPE_ID_EPARGNE  = 2
    const val BANK_TYPE_ID_PEA      = 3

    // Paramètres de compte bancaire (BankAccount.parameterId ←→ BankAccountParameter.id)
    const val BANK_PARAM_ID_STANDARD  = 1
    const val BANK_PARAM_ID_PREMIUM   = 2
    const val BANK_PARAM_ID_BLOQUED   = 3

    // IBANs (BankAccount.iban ←→ Operation.ibanTarget ←→ Beneficiary.ibanTarget)
    const val IBAN_ALICE_COURANT = "FR76 3000 6000 0112 3456 7890 189"
    const val IBAN_ALICE_EPARGNE = "FR76 3000 6000 0198 7654 3210 175"
    const val IBAN_BOB_COURANT   = "FR76 1027 8022 9100 0105 7010 043"
    const val IBAN_EXTERNE_1     = "DE89 3704 0044 0532 0130 00"

    // Token
    const val FAKE_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.fake.payload"

    // ── Rôles ─────────────────────────────────────────────────────────────────

    val roles: List<Role> = listOf(
        Role(id = ROLE_ID_ADMIN,    name = "ADMIN"),
        Role(id = ROLE_ID_CUSTOMER, name = "CUSTOMER"),
    )

    val roleAdmin: Role    = roles[0]
    val roleCustomer: Role = roles[1]

    // ── Informations personnelles ─────────────────────────────────────────────

    val personalInformations: List<PersonalInformation> = listOf(
        PersonalInformation(
            id          = PERSONAL_INFO_ID_ADMIN,
            firstname   = "Admin",
            lastname    = "Système",
            email       = "admin@edubank.fr",
            address     = "1 rue de la Banque, 75001 Paris",
            phoneNumber = "+33600000001",
        ),
        PersonalInformation(
            id          = PERSONAL_INFO_ID_ALICE,
            firstname   = "Alice",
            lastname    = "Dupont",
            email       = "alice.dupont@mail.fr",
            address     = "12 avenue des Lilas, 69003 Lyon",
            phoneNumber = "+33611223344",
        ),
        PersonalInformation(
            id          = PERSONAL_INFO_ID_BOB,
            firstname   = "Bob",
            lastname    = "Martin",
            email       = "bob.martin@mail.fr",
            address     = "8 rue du Port, 13002 Marseille",
            phoneNumber = "+33622334455",
        ),
    )

    // ── Comptes utilisateurs ──────────────────────────────────────────────────

    val accounts: List<Account> = listOf(
        Account(
            id             = ACCOUNT_ID_ADMIN,
            personalInfoId = PERSONAL_INFO_ID_ADMIN,
            roleId         = ROLE_ID_ADMIN,
            state          = AccountStateEnum.ACTIVE,
        ),
        Account(
            id             = ACCOUNT_ID_ALICE,
            personalInfoId = PERSONAL_INFO_ID_ALICE,
            roleId         = ROLE_ID_CUSTOMER,
            state          = AccountStateEnum.ACTIVE,
        ),
        Account(
            id             = ACCOUNT_ID_BOB,
            personalInfoId = PERSONAL_INFO_ID_BOB,
            roleId         = ROLE_ID_CUSTOMER,
            state          = AccountStateEnum.INACTIVE,
        ),
    )

    // ── Tokens ────────────────────────────────────────────────────────────────

    val tokenAdmin = TokenRequest(jwt = FAKE_JWT)

    val tokenResponseAdmin = TokenResponse(
        id   = ACCOUNT_ID_ADMIN,
        role = "ADMIN",
    )
    val tokenResponseAlice = TokenResponse(
        id   = ACCOUNT_ID_ALICE,
        role = "CUSTOMER",
    )

    // Credentials valides pour le fake login
    data class FakeCredential(val email: String, val password: String, val accountId: String, val role: String)

    val credentials: List<FakeCredential> = listOf(
        FakeCredential("admin@edubank.fr",      "Admin1234!",  ACCOUNT_ID_ADMIN, "ADMIN"),
        FakeCredential("alice.dupont@mail.fr",  "Alice1234!",  ACCOUNT_ID_ALICE, "CUSTOMER"),
        FakeCredential("bob.martin@mail.fr",    "Bob1234!",    ACCOUNT_ID_BOB,   "CUSTOMER"),
    )

    // ── Types de comptes bancaires ────────────────────────────────────────────

    val bankTypes: List<Type> = listOf(
        Type(id = BANK_TYPE_ID_COURANT, name = "Compte courant"),
        Type(id = BANK_TYPE_ID_EPARGNE, name = "Livret épargne"),
        Type(id = BANK_TYPE_ID_PEA,     name = "PEA"),
    )

    // ── Paramètres de comptes bancaires ──────────────────────────────────────

    val bankParameters: List<BankAccountParameter> = listOf(
        BankAccountParameter(
            id            = BANK_PARAM_ID_STANDARD,
            overdraftLimit = -500.0,
            state         = State.ACTIVE,
        ),
        BankAccountParameter(
            id            = BANK_PARAM_ID_PREMIUM,
            overdraftLimit = -2000.0,
            state         = State.ACTIVE,
        ),
        BankAccountParameter(
            id            = BANK_PARAM_ID_BLOQUED,
            overdraftLimit = 0.0,
            state         = State.BLOQUED,
        ),
    )

    // ── Comptes bancaires ─────────────────────────────────────────────────────

    val bankAccounts: List<BankAccount> = listOf(
        BankAccount(
            id          = "bank-0001",
            parameterId = BANK_PARAM_ID_STANDARD,
            typeId      = BANK_TYPE_ID_COURANT,
            sold        = 1_250.75,
            iban        = IBAN_ALICE_COURANT,
        ),
        BankAccount(
            id          = "bank-0002",
            parameterId = BANK_PARAM_ID_PREMIUM,
            typeId      = BANK_TYPE_ID_EPARGNE,
            sold        = 8_500.00,
            iban        = IBAN_ALICE_EPARGNE,
        ),
        BankAccount(
            id          = "bank-0003",
            parameterId = BANK_PARAM_ID_BLOQUED,
            typeId      = BANK_TYPE_ID_COURANT,
            sold        = -120.50,
            iban        = IBAN_BOB_COURANT,
        ),
    )

    val bankAccountDetails: List<BankAccountDetail> = listOf(
        BankAccountDetail(
            id        = "bank-0001",
            parameter = bankParameters[BANK_PARAM_ID_STANDARD - 1],
            type      = bankTypes[BANK_TYPE_ID_COURANT - 1],
            sold      = 1_250.75,
            iban      = IBAN_ALICE_COURANT,
        ),
        BankAccountDetail(
            id        = "bank-0002",
            parameter = bankParameters[BANK_PARAM_ID_PREMIUM - 1],
            type      = bankTypes[BANK_TYPE_ID_EPARGNE - 1],
            sold      = 8_500.00,
            iban      = IBAN_ALICE_EPARGNE,
        ),
        BankAccountDetail(
            id        = "bank-0003",
            parameter = bankParameters[BANK_PARAM_ID_BLOQUED - 1],
            type      = bankTypes[BANK_TYPE_ID_COURANT - 1],
            sold      = -120.50,
            iban      = IBAN_BOB_COURANT,
        ),
    )

    // ── Offres ────────────────────────────────────────────────────────────────

    val offers: List<Offer> = listOf(
        Offer(
            id          = 1,
            title       = "Livret boosté à 4%",
            description = "Profitez d'un taux boosté de 4% pendant 3 mois sur votre livret épargne.",
            state       = Offer.State.ACTIVE,
            startDate   = LocalDate.of(2025, 1, 1),
            endDate     = LocalDate.of(2025, 12, 31),
            picturePath = null,
        ),
        Offer(
            id          = 2,
            title       = "Carte premium offerte",
            description = "Obtenez votre carte bancaire premium sans frais la première année.",
            state       = Offer.State.ACTIVE,
            startDate   = LocalDate.of(2025, 3, 1),
            endDate     = LocalDate.of(2025, 9, 30),
            picturePath = null,
        ),
        Offer(
            id          = 3,
            title       = "Parrainage : 50€ offerts",
            description = "Parrainez un ami et recevez 50€ sur votre compte courant.",
            state       = Offer.State.INACTIVE,
            startDate   = LocalDate.of(2024, 6, 1),
            endDate     = LocalDate.of(2024, 12, 31),
            picturePath = null,
        ),
        Offer(
            id          = 4,
            title       = "Prêt immobilier à 2.9%",
            description = "Financement immobilier à taux fixe 2.9% jusqu'au 30 juin.",
            state       = Offer.State.EXPIRED,
            startDate   = LocalDate.of(2024, 1, 1),
            endDate     = LocalDate.of(2024, 6, 30),
            picturePath = null,
        ),
    )

    // ── Opérations ────────────────────────────────────────────────────────────
    // accountSourceId  ←→  Account.id
    // ibanTarget       ←→  BankAccount.iban

    val operations: List<Operation> = listOf(
        Operation(
            id              = 1,
            accountSourceId = ACCOUNT_ID_ALICE,
            label           = "Virement loyer mars",
            state           = OperationState.COMPLETED,
            ibanTarget      = IBAN_EXTERNE_1,
            amount          = 850.00,
            date            = OffsetDateTime.of(2025, 3, 1, 9, 0, 0, 0, ZoneOffset.UTC),
        ),
        Operation(
            id              = 2,
            accountSourceId = ACCOUNT_ID_ALICE,
            label           = "Remboursement Bob",
            state           = OperationState.COMPLETED,
            ibanTarget      = IBAN_BOB_COURANT,
            amount          = 45.50,
            date            = OffsetDateTime.of(2025, 3, 3, 14, 30, 0, 0, ZoneOffset.UTC),
        ),
        Operation(
            id              = 3,
            accountSourceId = ACCOUNT_ID_ALICE,
            label           = "Achat en ligne",
            state           = OperationState.PENDING,
            ibanTarget      = IBAN_EXTERNE_1,
            amount          = 129.99,
            date            = OffsetDateTime.of(2025, 3, 5, 11, 15, 0, 0, ZoneOffset.UTC),
        ),
        Operation(
            id              = 4,
            accountSourceId = ACCOUNT_ID_BOB,
            label           = "Abonnement streaming",
            state           = OperationState.FAILED,
            ibanTarget      = IBAN_EXTERNE_1,
            amount          = 15.99,
            date            = OffsetDateTime.of(2025, 3, 4, 8, 0, 0, 0, ZoneOffset.UTC),
        ),
    )

    // ── Bénéficiaires ─────────────────────────────────────────────────────────
    // accountSourceId  ←→  Account.id
    // ibanTarget       ←→  BankAccount.iban ou IBAN_EXTERNE

    val beneficiaries: List<Beneficiary> = listOf(
        Beneficiary(
            id              = 1,
            accountSourceId = ACCOUNT_ID_ALICE,
            ibanTarget      = IBAN_BOB_COURANT,
            name            = "Bob Martin",
        ),
        Beneficiary(
            id              = 2,
            accountSourceId = ACCOUNT_ID_ALICE,
            ibanTarget      = IBAN_EXTERNE_1,
            name            = "Propriétaire",
        ),
        Beneficiary(
            id              = 3,
            accountSourceId = ACCOUNT_ID_BOB,
            ibanTarget      = IBAN_ALICE_COURANT,
            name            = "Alice Dupont",
        ),
    )
}

