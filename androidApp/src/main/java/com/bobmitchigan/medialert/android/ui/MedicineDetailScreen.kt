package com.bobmitchigan.medialert.android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bobmitchigan.medialert.MR
import com.bobmitchigan.medialert.android.ui.component.LabelValue
import com.bobmitchigan.medialert.android.ui.component.PrimaryButton
import com.bobmitchigan.medialert.android.ui.component.navigateSingleTop
import com.bobmitchigan.medialert.data.MockMedicineRepository
import com.bobmitchigan.medialert.domain.Destination
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.domain.dateTimeNow
import com.bobmitchigan.medialert.viewModel.MedicineDetailViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun MedicineDetailScreen(
    medicineId: Int?,
    navigationController: NavHostController,
    viewModel: MedicineDetailViewModel = getViewModel { parametersOf(medicineId) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.let {
        DetailContent(it) {
            navigationController.navigateSingleTop(Destination.BlisterPacks(medicineId))
        }
    }
}

@Composable
fun DetailContent(state: Medicine, onTakePill: () -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                stringResource(id = MR.strings.medicine_detail.resourceId),
                style = MaterialTheme.typography.h6
            )
            VerticalSpacer()
            LabelValue(
                label = stringResource(id = MR.strings.medicine_detail_name.resourceId),
                state.name
            )
            LabelValue(
                label = stringResource(id = MR.strings.medicine_detail_pills_take_at.resourceId),
                values = state.schedule.map { it.toString() }.toTypedArray()
            )
            LabelValue(
                label = stringResource(id = MR.strings.medicine_list_eaten.resourceId),
                state.remainingCount().toString()
            )
            LabelValue(
                label = stringResource(id = MR.strings.medicine_list_remaining.resourceId),
                state.eatenCount().toString()
            )
            VerticalSpacer()
            PrimaryButton(
                text = stringResource(id = MR.strings.medicine_detail_take_pill.resourceId),
                onClick = onTakePill
            )
        }
    }
}

@Composable
private fun VerticalSpacer() {
    Spacer(modifier = Modifier.height(16.dp))
}

@Preview
@Composable
internal fun MedicineDetailPreview() {
    DetailContent(
        Medicine(
            name = "Name",
            blisterPacks = MockMedicineRepository.PREVIEW_BLISTER_PACKS,
            schedule = listOf(),
            firstPillDateTime = dateTimeNow()
        )
    ) {}
}
