package com.bobmitchigan.medialert.android.ui.component

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(text = text)
    }
}

@Preview
@Composable
internal fun PrimaryButtonPreview() {
    PrimaryButton(text = "Text") {
    }
}
