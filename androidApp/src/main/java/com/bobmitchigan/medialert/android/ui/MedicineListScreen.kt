package com.bobmitchigan.medialert.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bobmitchigan.medialert.MR
import com.bobmitchigan.medialert.android.design.theme.Typography
import com.bobmitchigan.medialert.data.MockMedicineRepository
import com.bobmitchigan.medialert.domain.Destination
import com.bobmitchigan.medialert.domain.Medicine
import com.bobmitchigan.medialert.viewModel.MedicineListViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun MedicineListScreen(
    navigationController: NavHostController,
    viewModel: MedicineListViewModel = getViewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    MedicineListContent(medicines = state) {
        navigationController.navigate(Destination.SingleMedicine(it).destination())
    }
}

@Composable
private fun MedicineListContent(
    medicines: List<Medicine>,
    navigateToDetail: (id: Int) -> Unit
) {
    LazyColumn {
        spacerItem(24.dp)
        items(medicines) { medicine ->
            Card(
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .clickable { navigateToDetail(medicine.id!!) }
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(text = medicine.name, style = Typography.h4)
                    Text(
                        text = "${
                            stringResource(
                                id = MR.strings.medicine_list_eaten.resourceId
                            )
                        } ${medicine.eatenCount()}",
                        style = Typography.body1
                    )
                    Text(
                        text = "${
                            stringResource(
                                id = MR.strings.medicine_list_remaining.resourceId
                            )
                        } ${medicine.remainingCount()}",
                        style = Typography.body1
                    )
                }
            }
        }
        spacerItem(32.dp)
    }
}

private fun LazyListScope.spacerItem(height: Dp) {
    item {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        )
    }
}

@Preview
@Composable
fun MedicineListScreenPreview() {
    MedicineListContent((0..1).map {
        Medicine("Name $it", MockMedicineRepository.PREVIEW_BLISTER_PACKS, listOf())
    }, {})
}
