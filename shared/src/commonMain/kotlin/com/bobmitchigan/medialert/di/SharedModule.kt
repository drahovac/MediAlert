package com.bobmitchigan.medialert.di

import com.bobmitchigan.medialert.data.Database
import com.bobmitchigan.medialert.data.MedicineRepositoryImpl
import com.bobmitchigan.medialert.data.NotificationSchedulerImpl
import com.bobmitchigan.medialert.domain.MedicineRepository
import com.bobmitchigan.medialert.domain.NotificationScheduler
import com.bobmitchigan.medialert.viewModel.BaseViewModel
import com.bobmitchigan.medialert.viewModel.BlisterPacksViewModel
import com.bobmitchigan.medialert.viewModel.CalendarViewModel
import com.bobmitchigan.medialert.viewModel.CreateMedicineViewModel
import com.bobmitchigan.medialert.viewModel.MedicineDetailViewModel
import com.bobmitchigan.medialert.viewModel.MedicineListViewModel
import com.bobmitchigan.medialert.viewModel.SplashViewModel
import com.rickclephas.kmm.viewmodel.KMMViewModel
import org.koin.core.context.startKoin
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

internal expect val platformModule: Module

internal val sharedModule = module {
    factory<MedicineRepository> {
        MedicineRepositoryImpl(get())
    }

    factory<NotificationScheduler> {
        NotificationSchedulerImpl()
    }

    single { Database(get()) }

    baseViewModel { SplashViewModel(get()) }

    kmmViewModel { CreateMedicineViewModel(get()) }

    baseViewModel { MedicineListViewModel(get()) }

    baseViewModel { CalendarViewModel(get()) }

    baseViewModel { params -> BlisterPacksViewModel(get(), params.getOrNull()) }

    baseViewModel { params -> MedicineDetailViewModel(get(), get(), params.getOrNull()) }
}

internal expect inline fun <reified T : BaseViewModel> Module.baseViewModel(
    noinline definition: Definition<T>
): KoinDefinition<T>

internal expect inline fun <reified T : KMMViewModel> Module.kmmViewModel(
    noinline definition: Definition<T>
): KoinDefinition<T>

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(platformModule, sharedModule)
    }
}
