package com.bobmitchigan.medialert.android.ui.component

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bobmitchigan.medialert.android.design.theme.Typography
import dev.icerock.moko.resources.StringResource
import com.bobmitchigan.medialert.MR

@Composable
fun ErrorLabel(error: StringResource) {
    Text(
        text = stringResource(id = error.resourceId),
        style = Typography.caption, color = MaterialTheme.colors.error
    )
}

@Preview
@Composable
fun ErrorLabel() {
    ErrorLabel(error = MR.strings.create_medicine_mandatory_field)
}
