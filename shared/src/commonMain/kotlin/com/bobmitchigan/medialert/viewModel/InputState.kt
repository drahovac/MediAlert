package com.bobmitchigan.medialert.viewModel

import dev.icerock.moko.resources.StringResource

data class InputState<T>(
    val value: T? = null,
    val error: StringResource? = null
)

fun <T>T?.toInputState() = InputState(value = this)
