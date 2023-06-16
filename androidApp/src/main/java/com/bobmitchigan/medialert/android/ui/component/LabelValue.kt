package com.bobmitchigan.medialert.android.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LabelValue(
    label: String,
    vararg values: String,
) {
    Text(
        text = label,
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.primaryVariant,
    )
    values.forEach { Text(text = it, style = MaterialTheme.typography.body1) }
}

@Preview
@Composable
fun LabelValuePreview() {
    MaterialTheme {
        Column {
            LabelValue(label = "Label", values = arrayOf("Value", "Value"))
        }
    }
}
