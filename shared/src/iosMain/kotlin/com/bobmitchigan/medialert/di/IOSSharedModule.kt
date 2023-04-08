package com.bobmitchigan.medialert.di

import com.bobmitchigan.medialert.viewModel.BaseViewModel
import org.koin.core.definition.Definition
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module

actual inline fun <reified T : BaseViewModel> Module.baseViewModel(
    noinline definition: Definition<T>
): Pair<Module, InstanceFactory<T>> {
    return factory(definition = definition)
}