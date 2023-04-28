@file:OptIn(ExperimentalMaterial3Api::class)

package com.bobmitchigan.medialert.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
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
import com.bobmitchigan.medialert.android.ui.component.ErrorLabel
import com.bobmitchigan.medialert.domain.Destination
import com.bobmitchigan.medialert.viewModel.*
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
            isError = state.name.error != null,
            supportingText = { state.name.error?.let { ErrorLabel(it) } },
            label = { Text(text = stringResource(MR.strings.create_medicine_name.resourceId)) })

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = state.blisterPackCount.value?.toString().orEmpty(),
            onValueChange = actions::updateBlisterPacksCount,
            isError = state.blisterPackCount.error != null,
            supportingText = { state.blisterPackCount.error?.let { ErrorLabel(it) } },
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

        val packCount = state.blisterPackCount.value ?: 0
        if (state.areAllPacksIdentical && packCount > 0) {
            BlisterCountInputs(state, actions)
        } else {
            (0 until packCount).forEach { BlisterCountInputs(state = state, actions = actions, it) }
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

@Composable
private fun BlisterCountInputs(
    state: CreateMedicineState,
    actions: CreateMedicineActions,
    packIndex: Int = 0,
) {
    Text(
        modifier = Modifier
            .padding(top = 8.dp)
            .padding(horizontal = 16.dp),
        style = Typography.overline,
        text = if (state.areAllPacksIdentical) {
            stringResource(id = MR.strings.create_medicine_all_dimension.resourceId)
        } else stringResource(id = MR.strings.create_medicine_dimension.resourceId, packIndex + 1)
    )

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
    ) {
        val dimension = state.dimensions[packIndex]

        OutlinedTextField(
            modifier = Modifier
                .weight(1f),
            value = dimension.rowCount.value?.toString().orEmpty(),
            onValueChange = { actions.updateRowCount(it, packIndex) },
            label = { Text(text = stringResource(MR.strings.create_medicine_row_count.resourceId)) },
            isError = dimension.rowCount.error != null,
            supportingText = { dimension.rowCount.error?.let { ErrorLabel(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        Spacer(modifier = Modifier.width(4.dp))

        OutlinedTextField(
            modifier = Modifier
                .weight(1f),
            value = dimension.columnCount.value?.toString().orEmpty(),
            onValueChange = { actions.updateColumnCount(it, packIndex) },
            isError = dimension.columnCount.error != null,
            supportingText = { dimension.columnCount.error?.let { ErrorLabel(it) } },
            label = { Text(text = stringResource(MR.strings.create_medicine_column_count.resourceId)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
    }
}

@Composable
fun InputState<*>.ErrorText() {
    error?.let { ErrorLabel(it) }
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
