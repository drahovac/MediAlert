package com.bobmitchigan.medialert.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
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
import com.bobmitchigan.medialert.domain.BlisterCavity
import com.bobmitchigan.medialert.domain.BlisterPack
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.viewModel.CavityCoordinates
import com.bobmitchigan.medialert.viewModel.MedicineDetailActions
import com.bobmitchigan.medialert.viewModel.MedicineDetailViewModel
import kotlinx.datetime.toJavaLocalDateTime
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import java.time.format.DateTimeFormatter

@Composable
internal fun MedicineDetailScreen(
    medicineId: Int?,
    viewModel: MedicineDetailViewModel = getViewModel { parametersOf(medicineId) }
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.selectedCavity?.let {
        DetailDialog(it, viewModel)
    }
    state?.let { MedicineDetailContent(medicine = it.medicine, viewModel) }
}

@Composable
private fun DetailDialog(
    cavity: BlisterCavity,
    actions: MedicineDetailActions
) {
    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        title = { Text(text = cavity.longName()) },
        text = { Text(text = cavity.desc()) },
        onDismissRequest = actions::clearSelectedCavity,
        buttons = {
            when (cavity) {
                is BlisterCavity.EATEN -> {}
                BlisterCavity.FILLED -> {
                    Button(
                        modifier = Modifier.padding(16.dp),
                        onClick = actions::consumeSelected
                    ) {
                        Text(text = stringResource(id = MR.strings.medicine_detail_consume.resourceId))
                    }
                    Button(
                        modifier = Modifier.padding(16.dp),
                        onClick = actions::setLostSelected
                    ) {
                        Text(text = stringResource(id = MR.strings.medicine_detail_lost.resourceId))
                    }
                }
                BlisterCavity.LOST -> {}
                BlisterCavity.NONE -> {}
            }
        })
}

@Composable
private fun BlisterCavity.desc(): String {
    return when (this) {
        is BlisterCavity.EATEN -> this.taken.toJavaLocalDateTime()
            .format(DateTimeFormatter.ISO_DATE_TIME)
        BlisterCavity.FILLED -> ""
        BlisterCavity.LOST -> stringResource(id = MR.strings.medicine_detail_marked_as_lost.resourceId)
        BlisterCavity.NONE -> ""
    }
}

@Composable
private fun BlisterCavity.longName(): String {
    return when (this) {
        is BlisterCavity.EATEN -> stringResource(id = MR.strings.medicine_detail_eaten.resourceId)
        BlisterCavity.FILLED -> stringResource(id = MR.strings.medicine_detail_filled.resourceId)
        BlisterCavity.LOST -> stringResource(id = MR.strings.medicine_detail_lost.resourceId)
        BlisterCavity.NONE -> ""
    }
}

@Composable
private fun MedicineDetailContent(
    medicine: Medicine,
    actions: MedicineDetailActions
) {
    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Medicine: ${medicine.name}", style = Typography.h4)

        medicine.blisterPacks.forEachIndexed { index, pack ->
            BlisterPackView(index, pack, actions)
        }
    }
}

@Composable
private fun BlisterPackView(index: Int, pack: BlisterPack, actions: MedicineDetailActions) {
    Column {
        Text(
            text = "${stringResource(MR.strings.medicine_detail_pack.resourceId)} ${index + 1}",
            style = Typography.h4
        )
        pack.rows.forEachIndexed { rowIndex, row ->
            Row(Modifier.fillMaxWidth()) {
                row.value.forEachIndexed { cavityIndex, cavity ->
                    Card(
                        Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = cavity.shortName,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    actions.selectCavity(
                                        CavityCoordinates(
                                            blisterPack = index,
                                            rowIndex = rowIndex,
                                            cavityIndex = cavityIndex
                                        )
                                    )
                                },
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
        ),
        actions = ActionsInvocationHandler.createActionsProxy(),
    )
}

@Preview
@Composable
internal fun MedicineDetailDialogPreview() {
    DetailDialog(
        cavity = BlisterCavity.FILLED,
        actions = ActionsInvocationHandler.createActionsProxy()
    )
}
