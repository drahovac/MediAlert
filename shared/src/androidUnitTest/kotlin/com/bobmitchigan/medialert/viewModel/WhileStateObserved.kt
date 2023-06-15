package com.bobmitchigan.medialert.viewModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

/**
 * Runs a test on the viewModel state while its value is being collected. This is useful for cases
 * when [started] is [SharingStarted.WhileSubscribed] to simulate data being observed.
 *
 * @param state The viewModel state to test.
 * @param runTest A function that performs the test.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> whileStateObserved(state: StateFlow<T>, runTest: () -> Unit) =
    runTest(UnconfinedTestDispatcher()) {
        val job = launch { state.collect() }
        runTest()
        job.cancel()
    }
