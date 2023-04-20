package com.bobmitchigan.medialert.android.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.bobmitchigan.medialert.viewModel.MedicineDetailViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MedicineDetailScreen(
    medicineId: Int?,
    viewModel: MedicineDetailViewModel = getViewModel { parametersOf(medicineId) }
) {

    Text(text = "ID: $medicineId")
}
