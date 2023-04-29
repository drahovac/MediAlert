@file:OptIn(ExperimentalMaterial3Api::class)

package com.bobmitchigan.medialert.android.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.bobmitchigan.medialert.MR
import com.bobmitchigan.medialert.viewModel.InputState

@Composable
fun OutlinedStringInput(
    state: InputState<String>,
    onValueChanged: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier,
        value = state.value.orEmpty(),
        onValueChange = { onValueChanged(it) },
        isError = state.error != null,
        supportingText = { state.error?.let { ErrorLabel(it) } },
        label = { Text(text = label) },
    )
}

@Composable
fun OutlinedIntInput(
    state: InputState<Int>,
    onValueChanged: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier,
        value = state.value?.toString().orEmpty(),
        onValueChange = { onValueChanged(it) },
        isError = state.error != null,
        supportingText = { state.error?.let { ErrorLabel(it) } },
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}

@Preview
@Composable
fun OutlinedStringInputPreview() {
    MaterialTheme {
        Column {
            OutlinedStringInput(
                state = InputState("Value"),
                onValueChanged = {},
                label = "Label"
            )
            OutlinedStringInput(
                state = InputState(
                    "Value",
                    MR.strings.create_medicine_mandatory_field
                ), onValueChanged = {}, label = "Label"
            )
        }
    }
}
