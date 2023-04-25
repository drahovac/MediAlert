package com.bobmitchigan.medialert.di

import com.bobmitchigan.medialert.data.Database
import com.bobmitchigan.medialert.data.MedicineRepositoryImpl
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.viewModel.*
import org.koin.core.context.startKoin
import org.koin.core.definition.Definition
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

internal expect val platformModule: Module

internal val sharedModule = module {
    factory<MedicineRepository> {
        MedicineRepositoryImpl(get())
    }

    single { Database(get()) }

    baseViewModel { SplashViewModel(get()) }

    baseViewModel { CreateMedicineViewModel(get()) }

    baseViewModel { MedicineListViewModel(get()) }

    baseViewModel { params -> MedicineDetailViewModel(get(), params.getOrNull()) }
}

internal expect inline fun <reified T : BaseViewModel> Module.baseViewModel(
    noinline definition: Definition<T>
): Pair<Module, InstanceFactory<T>>

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(platformModule, sharedModule)
    }
}
