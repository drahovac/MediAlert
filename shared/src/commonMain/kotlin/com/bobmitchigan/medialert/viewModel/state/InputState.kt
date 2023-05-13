package com.bobmitchigan.medialert.viewModel.state

import com.bobmitchigan.medialert.viewModel.CommonSerializable
import dev.icerock.moko.resources.StringResource
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class InputState<T : Any>(
    val value: T? = null,
    @Transient @kotlin.jvm.Transient val error: StringResource? = null
) : CommonSerializable

fun <T : Any> T?.toInputState() = InputState(value = this)
