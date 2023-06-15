package com.bobmitchigan.medialert.android.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bobmitchigan.medialert.data.MockMedicineRepository
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

    DetailContent(state)
}

@Composable
fun DetailContent(state: Medicine?) {
    Text(text = "Todo detail $state")
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
    )
}
