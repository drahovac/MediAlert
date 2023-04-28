package com.bobmitchigan.medialert.android.ui.component

import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(text = text)
    }
}

@Preview
@Composable
fun SecondaryButtonPreview() {
    SecondaryButton(text = "Text") {
    }
}
