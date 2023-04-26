package com.bobmitchigan.medialert.android.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bobmitchigan.medialert.MR
import com.bobmitchigan.medialert.android.R

@Composable
fun BoxScope.CreateMedicineFab(navigate: () -> Unit) {
    ExtendedFloatingActionButton(
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_add_24),
                contentDescription = null
            )
        },
        modifier = Modifier
            .padding(bottom = 32.dp, end = 16.dp)
            .align(Alignment.BottomEnd),
        text = { Text(text = stringResource(id = MR.strings.medicine_detail_add_new.resourceId)) },
        onClick = navigate,
    )
}

@Preview
@Composable
fun CreateMedicineFabPreview() {
    Box {
        CreateMedicineFab {}
    }
}
