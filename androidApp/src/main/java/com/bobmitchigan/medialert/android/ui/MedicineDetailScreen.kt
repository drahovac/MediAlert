package com.bobmitchigan.medialert.android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bobmitchigan.medialert.MR
import com.bobmitchigan.medialert.android.design.theme.Typography
import com.bobmitchigan.medialert.data.MockMedicineRepository.Companion.PREVIEW_BLISTER_PACKS
import com.bobmitchigan.medialert.domain.BlisterPack
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.viewModel.MedicineDetailViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MedicineDetailScreen(
    medicineId: Int?,
    viewModel: MedicineDetailViewModel = getViewModel { parametersOf(medicineId) }
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.let { MedicineDetailContent(medicine = it) }
}

@Composable
private fun MedicineDetailContent(medicine: Medicine) {
    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Medicine: ${medicine.name}", style = Typography.h4)

        medicine.blisterPacks.forEachIndexed { index, pack ->
            BlisterPackView(index, pack)
        }
    }
}

@Composable
private fun BlisterPackView(index: Int, pack: BlisterPack) {
    Column {
        Text(
            text = "${stringResource(MR.strings.medicine_detail_pack.resourceId)} ${index + 1}",
            style = Typography.h4
        )
        pack.rows.forEach {
            Row(Modifier.fillMaxWidth()) {
                it.value.forEach {
                    Card(
                        Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = it.shortName,
                            modifier = Modifier.padding(8.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                }
            }
        }
    }
}

@Preview
@Composable
internal fun MedicineDetailScreenPreview() {
    MedicineDetailContent(
        medicine = Medicine(
            name = "Name",
            blisterPacks = PREVIEW_BLISTER_PACKS,
            schedule = listOf()
        )
    )
}
