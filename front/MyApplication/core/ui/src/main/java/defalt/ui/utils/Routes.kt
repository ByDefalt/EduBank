package defalt.ui.utils

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    object Core : Routes() {

        @Serializable
        object Home : Routes()

        @Serializable
        object AdminHome : Routes()

        @Serializable
        object Menu : Routes()
    }

    @Serializable
    object Admin : Routes() {
        @Serializable object AccountList : Routes()

        @Serializable data class AccountDetail(val id: String) : Routes()

        @Serializable object BankList : Routes()

        @Serializable data class BankDetail(val id: String) : Routes()

        @Serializable object CreateBankAccount : Routes()

        @Serializable object OfferList : Routes()

        @Serializable data class OfferDetail(val id: Int) : Routes()

        @Serializable object CreateOffer : Routes()

        @Serializable object OperationList : Routes()

        @Serializable data class OperationDetail(val id: Int) : Routes()
    }

    @Serializable
    object Account : Routes() {
        @Serializable
        object Login : Routes()

        @Serializable
        object Register : Routes()
    }

    @Serializable
    object Bank : Routes() {
        @Serializable
        object Home : Routes()

        @Serializable
        object ListAccount : Routes()

        @Serializable
        data class AccountDetails(val accountId: String) : Routes()
    }

    @Serializable
    object Offer : Routes() {
        @Serializable
        object List : Routes()
    }

    @Serializable
    object Operation : Routes() {

        @Serializable
        object BottomSheet : Routes()

        @Serializable
        object Beneficiaire : Routes()

        @Serializable
        object BeneficiaireGraph : Routes()

        @Serializable
        object AddBeneficiaire : Routes()

        @Serializable
        data class EditBeneficiaire(val id: Int) : Routes()

        /** Wizard de création d'un virement (sous-graphe partagé) */
        @Serializable
        object CreateTransfer : Routes() {

            /** Étape 1 : choix du compte à débiter */
            @Serializable
            object Debit : Routes()

            /** Étape 2 : choix du destinataire */
            @Serializable
            object Receiver : Routes()

            /** Étape 3 : saisie du montant */
            @Serializable
            object Amount : Routes()

            /** Étape 4 : saisie du libellé */
            @Serializable
            object Label : Routes()

            /** Étape 5 : récapitulatif et confirmation */
            @Serializable
            object Recap : Routes()
        }
    }
}
