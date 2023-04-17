package com.bobmitchigan.medialert.di

import com.bobmitchigan.medialert.data.MockMedicineRepository
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.viewModel.BaseViewModel
import com.bobmitchigan.medialert.viewModel.CreateMedicineViewModel
import com.bobmitchigan.medialert.viewModel.SplashViewModel
import org.koin.core.context.startKoin
import org.koin.core.definition.Definition
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import org.koin.dsl.module

val sharedModule = module {
    single<MedicineRepository> {
        MockMedicineRepository()
    }

    baseViewModel { SplashViewModel(get()) }

    baseViewModel { CreateMedicineViewModel(get()) }
}

expect inline fun <reified T : BaseViewModel> Module.baseViewModel(
    noinline definition: Definition<T>
): Pair<Module, InstanceFactory<T>>

fun initKoin() {
    startKoin {
        modules(sharedModule)
    }
}
