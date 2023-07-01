package com.bobmitchigan.medialert.di

import com.bobmitchigan.medialert.data.DatabaseDriver
import com.bobmitchigan.medialert.viewModel.BaseViewModel
import com.bobmitchigan.medialert.viewModel.CreateMedicineSaveStateViewModel
import com.rickclephas.kmm.viewmodel.KMMViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual inline fun <reified T : BaseViewModel> Module.baseViewModel(
    noinline definition: Definition<T>
): KoinDefinition<T> {
    return viewModel(definition = definition)
}

actual inline fun <reified T : KMMViewModel> Module.kmmViewModel(
    noinline definition: Definition<T>
): KoinDefinition<T> {
    return viewModel(definition = definition)
}

actual val platformModule: Module = module {
    single { DatabaseDriver(androidContext()) }

    kmmViewModel { params -> CreateMedicineSaveStateViewModel(get(), params.getOrNull(), get()) }
}
