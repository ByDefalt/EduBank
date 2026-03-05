package defalt.featureOperation.viewModel

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureOperationViewModelModule = module {
    viewModel { BeneficiariesViewModel() }
    viewModel { CreateTransferDebitViewModel() }
    viewModel { CreateTransferReceiverViewModel() }
}
