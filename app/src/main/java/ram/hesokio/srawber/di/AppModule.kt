package ram.hesokio.srawber.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ram.hesokio.srawber.presentation.view_model.MainViewModel

val appModule = module {
    viewModel { MainViewModel(get()) }
}