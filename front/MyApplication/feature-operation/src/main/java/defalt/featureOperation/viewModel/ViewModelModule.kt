package defalt.featureOperation.viewModel

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureOperationViewModelModule = module {
    viewModel { BeneficiariesViewModel(get()) }
    viewModel { AddBeneficiaryViewModel(get()) }
    viewModel { EditBeneficiaryViewModel(get(), get(), get()) }
    // VM partagé pour tout le wizard de création de virement (étapes Débit → Destinataire → Montant → Libellé → Récap)
    viewModel { CreateTransferViewModel(get(), get(), get()) }
}
