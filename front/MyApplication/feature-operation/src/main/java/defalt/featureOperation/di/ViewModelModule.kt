package defalt.featureOperation.di

import defalt.featureOperation.viewModel.BeneficiariesViewModel
import defalt.featureOperation.viewModel.CreateTransferDebitViewModel
import defalt.featureOperation.viewModel.CreateTransferReceiverViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel { BeneficiariesViewModel() }
        viewModel { CreateTransferDebitViewModel() }
        viewModel { CreateTransferReceiverViewModel() }
    }
