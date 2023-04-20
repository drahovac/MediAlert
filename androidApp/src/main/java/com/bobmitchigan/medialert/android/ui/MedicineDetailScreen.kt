package com.bobmitchigan.medialert.android.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bobmitchigan.medialert.viewModel.MedicineDetailViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MedicineDetailScreen(
    medicineId: Int?,
    viewModel: MedicineDetailViewModel = getViewModel { parametersOf(medicineId) }
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Text(text = "Medicine: ${state?.name}")
}
