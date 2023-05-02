@file:OptIn(ExperimentalMaterial3Api::class)

package com.bobmitchigan.medialert.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.bobmitchigan.medialert.MR
import com.bobmitchigan.medialert.android.design.theme.Typography
import com.bobmitchigan.medialert.android.ui.component.OutlinedIntInput
import com.bobmitchigan.medialert.android.ui.component.OutlinedStringInput
import com.bobmitchigan.medialert.domain.Destination
import com.bobmitchigan.medialert.viewModel.*
import kotlinx.datetime.LocalTime
import org.koin.androidx.compose.getViewModel

@Composable
fun CreateMedicineScreen(
    navController: NavController,
    viewModel: CreateMedicineSaveStateViewModel = getViewModel(),
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
        BaseInfoInputs(state, actions)
        AllIdenticalCheckBox(actions, state)
        DimensionInputs(state, actions)
        ScheduleInputs(state, actions)
        SubmitButton(actions)
    }
}

@Composable
private fun AllIdenticalCheckBox(
    actions: CreateMedicineActions,
    state: CreateMedicineState
) {
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
}

@Composable
private fun BaseInfoInputs(
    state: CreateMedicineState,
    actions: CreateMedicineActions
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.surface)
            .padding(vertical = 4.dp)
    ) {
        OutlinedStringInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            state = state.name,
            label = stringResource(MR.strings.create_medicine_name.resourceId),
            onValueChanged = actions::updateName,
        )
        OutlinedIntInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            state = state.blisterPackCount,
            label = stringResource(MR.strings.create_medicine_blister_pack_count.resourceId),
            onValueChanged = actions::updateBlisterPacksCount,
        )
    }
}

@Composable
private fun SubmitButton(actions: CreateMedicineActions) {
    Button(
        onClick = { actions.submit() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = stringResource(id = MR.strings.create_medicine_save.resourceId))
    }
}

@Composable
private fun DimensionInputs(
    state: CreateMedicineState,
    actions: CreateMedicineActions
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.surface)
    ) {
        val packCount = state.blisterPackCount.value ?: 0
        if (state.areAllPacksIdentical && packCount > 0) {
            BlisterCountInputs(state, actions)
        } else {
            (0 until packCount).forEach {
                BlisterCountInputs(
                    state = state,
                    actions = actions,
                    it
                )
            }
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
            .padding(horizontal = 8.dp)
            .padding(bottom = 8.dp)
    ) {
        val dimension = state.dimensions[packIndex]

        OutlinedIntInput(
            modifier = Modifier.weight(1f),
            state = dimension.rowCount,
            onValueChanged = { actions.updateRowCount(it, packIndex) },
            label = stringResource(MR.strings.create_medicine_row_count.resourceId)
        )

        Spacer(modifier = Modifier.width(4.dp))

        OutlinedIntInput(
            modifier = Modifier.weight(1f),
            state = dimension.columnCount,
            onValueChanged = { actions.updateColumnCount(it, packIndex) },
            label = stringResource(MR.strings.create_medicine_column_count.resourceId)
        )
    }
}

@Composable
fun ScheduleInputs(state: CreateMedicineState, actions: CreateMedicineActions) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = 8.dp)
    ) {
        OutlinedIntInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            state = state.timesPerDay,
            onValueChanged = actions::updateTimesPerDay,
            label = stringResource(id = MR.strings.create_medicine_daily_intake.resourceId)
        )

        state.timeSchedule.forEachIndexed { index, time ->
            val ordinal = "${index + 1}" // TODO ordinal numbers
            val timePickerState =
                rememberTimePickerState(
                    time.value?.hour ?: 0,
                    time.value?.minute ?: 0,
                    is24Hour = true
                )
            observeTimeSelected(timePickerState, actions, index)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(
                        id = MR.strings.create_medicine_daily_time.resourceId,
                        ordinal,
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 32.dp),
                    style = Typography.overline
                )
                TimeInput(
                    state = timePickerState
                )
            }
        }
    }
}

@Composable
private fun observeTimeSelected(
    timePickerState: TimePickerState,
    actions: CreateMedicineActions,
    index: Int
) {
    LaunchedEffect(key1 = timePickerState.hour, timePickerState.minute) {
        actions.updateTimeSchedule(
            index,
            LocalTime(timePickerState.hour, timePickerState.minute)
        )
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
