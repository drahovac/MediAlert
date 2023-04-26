package com.bobmitchigan.medialert.viewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
ViewModel responsible for handling navigation events.
Event is just boolean flag, true means navigate, should be cleared after navigation is performed.
 */
open class NavigationViewModel : BaseViewModel() {

    private val _navigationEvent = MutableStateFlow(false)
    val navigationEvent = _navigationEvent.asStateFlow()

    /**
     * Call from consumer of navigation so event is not triggered multiple times.
     */
    fun clearEvent() = _navigationEvent.update { false }

    protected fun navigate() = _navigationEvent.update { true }
}
