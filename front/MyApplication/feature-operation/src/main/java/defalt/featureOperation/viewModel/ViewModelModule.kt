package defalt.featureOperation.viewModel

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureOperationViewModelModule = module {
    viewModel { BeneficiariesViewModel(get()) }
    viewModel { AddBeneficiaryViewModel(get()) }
    viewModel { EditBeneficiaryViewModel(get(), get(), get()) }
    viewModel { CreateTransferViewModel(get(), get(), get()) }

    viewModel { AdminOperationListViewModel(get()) }
    viewModel { AdminOperationDetailViewModel(get(), get(), get()) }
}
