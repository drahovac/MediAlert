package com.bobmitchigan.medialert.viewModel

import kotlinx.coroutines.CoroutineScope

actual open class BaseViewModel {
    actual val scope: CoroutineScope
        get() = TODO("Not yet implemented https://github.com/doublesymmetry/multiplatform-viewmodel/blob/main/src/darwinMain/kotlin/com/doublesymmetry/viewmodel/ViewModel.kt")
}