package com.bobmitchigan.medialert.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bobmitchigan.medialert.android.R
import com.bobmitchigan.medialert.android.design.theme.Typography
import com.bobmitchigan.medialert.viewModel.CreateMedicineActions
import com.bobmitchigan.medialert.viewModel.CreateMedicineState
import com.bobmitchigan.medialert.viewModel.CreateMedicineViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun CreateMedicineScreen(viewModel: CreateMedicineViewModel = getViewModel()) {

    val state: CreateMedicineState by viewModel.state.collectAsState()

    CreateMedicineContent(state, viewModel)
}

@Composable
private fun CreateMedicineContent(
    state: CreateMedicineState,
    actions: CreateMedicineActions
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Text(
            text = stringResource(R.string.create_medicine_title),
            style = Typography.h4,
            modifier = Modifier.padding(16.dp)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = state.name.orEmpty(),
            onValueChange = { actions.updateName(it) },
            label = { Text(text = stringResource(R.string.create_medicine_name)) })

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = state.blisterPackCount?.toString().orEmpty(),
            onValueChange = actions::updateBlisterPacksCount,
            label = { Text(text = stringResource(R.string.create_medicine_blister_pack_count)) },
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
            Text(text = stringResource(R.string.create_medicine_identical))
        }

        Row(Modifier.fillMaxWidth()) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                value = state.rowCount?.toString().orEmpty(),
                onValueChange = actions::updateRowCount,
                label = { Text(text = stringResource(R.string.create_medicine_row_count)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                value = state.columnCount?.toString().orEmpty(),
                onValueChange = actions::updateColumnCount,
                label = { Text(text = stringResource(R.string.create_medicine_column_count)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }
    }
}

@Preview
@Composable
fun CreateMedicineScreenPreview() {
    MaterialTheme {
        CreateMedicineContent(
            CreateMedicineState(
                name = "Name",
                blisterPackCount = 3,
                areAllPacksIdentical = true,
            ),
            object : CreateMedicineActions {
                override fun updateName(name: String) {
                }

                override fun updateBlisterPacksCount(count: String) {
                }

                override fun updateAllPacksIdentical() {
                }

                override fun updateRowCount(count: String) {
                }

                override fun updateColumnCount(count: String) {
                }
            }
        )
    }
}
