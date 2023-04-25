package com.bobmitchigan.medialert.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.bobmitchigan.medialert.MR
import com.bobmitchigan.medialert.android.design.theme.Typography
import com.bobmitchigan.medialert.domain.Destination
import com.bobmitchigan.medialert.viewModel.CreateMedicineActions
import com.bobmitchigan.medialert.viewModel.CreateMedicineState
import com.bobmitchigan.medialert.viewModel.CreateMedicineViewModel
import com.bobmitchigan.medialert.viewModel.toInputState
import org.koin.androidx.compose.getViewModel

@Composable
fun CreateMedicineScreen(
    navController: NavController,
    viewModel: CreateMedicineViewModel = getViewModel(),
) {
    val state: CreateMedicineState by viewModel.state.collectAsStateWithLifecycle()
    val navEvent: Boolean by viewModel.navigationEvent.collectAsStateWithLifecycle()

    CreateMedicineContent(state, viewModel)
    LaunchedEffect(key1 = navEvent) {
        if (navEvent) {
            navController.navigate(Destination.MedicineList.destination(), navOptions {
                launchSingleTop = true
                popUpTo(Destination.MedicineList.destination())
            })
            viewModel.clearEvent()
        }
    }
}

@Composable
private fun CreateMedicineContent(
    state: CreateMedicineState,
    actions: CreateMedicineActions,
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Text(
            text = stringResource(MR.strings.create_medicine_name.resourceId),
            style = Typography.h4,
            modifier = Modifier.padding(16.dp)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = state.name.value.orEmpty(),
            onValueChange = { actions.updateName(it) },
            label = { Text(text = stringResource(MR.strings.create_medicine_name.resourceId)) })

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = state.blisterPackCount.value?.toString().orEmpty(),
            onValueChange = actions::updateBlisterPacksCount,
            label = { Text(text = stringResource(MR.strings.create_medicine_blister_pack_count.resourceId)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .clickable { actions.updateAllPacksIdentical() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = state.areAllPacksIdentical,
                onCheckedChange = { actions.updateAllPacksIdentical() },
                enabled = true,
            )
            Text(text = stringResource(MR.strings.create_medicine_identical.resourceId))
        }

        Row(Modifier.fillMaxWidth()) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                value = state.rowCount.value?.toString().orEmpty(),
                onValueChange = actions::updateRowCount,
                label = { Text(text = stringResource(MR.strings.create_medicine_row_count.resourceId)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                value = state.columnCount.value?.toString().orEmpty(),
                onValueChange = actions::updateColumnCount,
                label = { Text(text = stringResource(MR.strings.create_medicine_column_count.resourceId)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }

        Button(
            onClick = { actions.submit() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = stringResource(id = MR.strings.create_medicine_save.resourceId))
        }
    }
}

@Preview
@Composable
fun CreateMedicineScreenPreview() {
    MaterialTheme {
        CreateMedicineContent(
            CreateMedicineState(
                name = "Name".toInputState(),
                blisterPackCount = 3.toInputState(),
                areAllPacksIdentical = true,
            ),
            ActionsInvocationHandler.createActionsProxy(),
        )
    }
}
