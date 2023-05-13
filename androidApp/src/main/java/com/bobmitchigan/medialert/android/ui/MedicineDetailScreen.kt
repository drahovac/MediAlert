package com.bobmitchigan.medialert.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bobmitchigan.medialert.MR
import com.bobmitchigan.medialert.android.design.theme.Typography
import com.bobmitchigan.medialert.android.ui.component.PrimaryButton
import com.bobmitchigan.medialert.android.ui.component.SecondaryButton
import com.bobmitchigan.medialert.data.MockMedicineRepository.Companion.PREVIEW_BLISTER_PACKS
import com.bobmitchigan.medialert.domain.BlisterCavity
import com.bobmitchigan.medialert.domain.BlisterPack
import com.bobmitchigan.medialert.domain.Destination
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.viewModel.state.CavityCoordinates
import com.bobmitchigan.medialert.viewModel.MedicineDetailActions
import com.bobmitchigan.medialert.viewModel.MedicineDetailViewModel
import kotlinx.datetime.toJavaLocalDateTime
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import java.time.format.DateTimeFormatter

@Composable
internal fun MedicineDetailScreen(
    medicineId: Int?,
    navigationController: NavHostController,
    viewModel: MedicineDetailViewModel = getViewModel { parametersOf(medicineId) }
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.selectedCavity?.let {
        DetailDialog(it, viewModel)
    }
    state?.let {
        MedicineDetailContent(medicine = it.medicine, viewModel) {
            navigationController.navigate(Destination.CreateMedicine.destination())
        }
    }
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
        title = { Text(text = cavity.longName(), style = Typography.h6) },
        text = { Text(text = cavity.desc()) },
        onDismissRequest = actions::clearSelectedCavity,
        confirmButton = {
            if (cavity is BlisterCavity.FILLED) {
                PrimaryButton(
                    text = stringResource(id = MR.strings.medicine_detail_consume.resourceId),
                    onClick = actions::consumeSelected
                )
            }
        },
        dismissButton = {
            if (cavity is BlisterCavity.FILLED) {
                SecondaryButton(
                    text = stringResource(id = MR.strings.medicine_detail_mark_lost.resourceId),
                    onClick = actions::setLostSelected
                )
            }
        },
    )
}

@Composable
private fun BlisterCavity.desc(): String {
    return when (this) {
        is BlisterCavity.EATEN -> stringResource(
            id = MR.strings.medicine_detail_eaten_desc.resourceId,
            this.taken.toJavaLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE)
        )
        BlisterCavity.FILLED -> stringResource(id = MR.strings.medicine_detail_filled_desc.resourceId)
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
    actions: MedicineDetailActions,
    navigate: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = medicine.name,
                style = Typography.h4,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            medicine.blisterPacks.forEachIndexed { index, pack ->
                BlisterPackView(index, pack, actions)
            }
            Spacer(modifier = Modifier.height(96.dp))
        }

        CreateMedicineFab(navigate)
    }
}

@Composable
private fun BlisterPackView(index: Int, pack: BlisterPack, actions: MedicineDetailActions) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .padding(bottom = 16.dp)
    ) {
        Column(Modifier.padding(8.dp)) {
            Text(
                text = "${stringResource(MR.strings.medicine_detail_pack.resourceId)} ${index + 1}",
                style = Typography.subtitle1
            )
            pack.rows.forEachIndexed { rowIndex, row ->
                Row(Modifier.fillMaxWidth()) {
                    row.value.forEachIndexed { cavityIndex, cavity ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            backgroundColor = MaterialTheme.colors.background,
                        ) {
                            Text(
                                text = cavity.shortName,
                                modifier = Modifier
                                    .clickable {
                                        actions.selectCavity(
                                            CavityCoordinates(
                                                blisterPack = index,
                                                rowIndex = rowIndex,
                                                cavityIndex = cavityIndex
                                            )
                                        )
                                    }
                                    .padding(8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
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
        navigate = {}
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
