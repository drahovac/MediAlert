package com.bobmitchigan.medialert.viewModel.state

import com.bobmitchigan.medialert.domain.BlisterCavity
import com.bobmitchigan.medialert.domain.Medicine

/**
 * A class representing the ui state of the medicine detail screen.
 *
 * @property medicine the medicine being displayed
 * @property selectedCavity the selected cavity for the medicine, or null if no cavity is selected
 */
data class MedicineDetailState(
    val medicine: Medicine,
    val selectedCavity: BlisterCavity?
)

/**
 * A class representing the coordinates of a cavity in a blister pack.
 *
 * @property blisterPack the index of the blister pack containing the cavity
 * @property rowIndex the index of the row within the blister pack
 * @property cavityIndex the index of the cavity within the row
 */
data class CavityCoordinates(
    val blisterPack: Int,
    val rowIndex: Int,
    val cavityIndex: Int,
)
