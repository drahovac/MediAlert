package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.domain.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MedicineDetailViewModel(
    private val medicineRepository: MedicineRepository,
    private val medicineId: Int? = null
) : MedicineDetailActions, BaseViewModel() {

    private val _state: MutableStateFlow<MedicineDetailState?> = MutableStateFlow(null)
    private var selectedCoordinates: CavityCoordinates? = null
    val state: StateFlow<MedicineDetailState?> = _state

    init {
        scope.launch {
            medicineRepository.getMedicineDetail(medicineId).collect {
                it?.let {
                    _state.value = MedicineDetailState(
                        medicine = it,
                        selectedCavity = null
                    )
                }
            }
        }
    }

    override fun selectCavity(coordinates: CavityCoordinates) {
        selectedCoordinates = coordinates
        _state.update {
            it?.copy(selectedCavity = findCavity(it.medicine.blisterPacks, coordinates))
        }
    }

    override fun clearSelectedCavity() {
        _state.update { it?.copy(selectedCavity = null) }
    }

    override fun consumeSelected() {
        updateSelectedAndClear(
            BlisterCavity.EATEN(
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
        )
    }

    private fun updateSelectedAndClear(cavity: BlisterCavity) {
        selectedCoordinates?.let {
            scope.launch {
                updateCavity(it, cavity)?.let { medicineRepository.updateMedicine(it) }
                clearSelectedCavity()
            }
        }
        clearSelectedCavity()
    }

    override fun setLostSelected() = updateSelectedAndClear(BlisterCavity.LOST)

    private fun findCavity(
        blisterPacks: List<BlisterPack>,
        coordinates: CavityCoordinates
    ): BlisterCavity {
        return blisterPacks[coordinates.blisterPack].rows[coordinates.rowIndex].value[coordinates.cavityIndex]
    }

    private fun updateCavity(coordinates: CavityCoordinates, cavity: BlisterCavity): Medicine? {
        return state.value?.medicine?.let {
            it.copy(
                blisterPacks = it.blisterPacks.update(coordinates, cavity)
            )
        }
    }
}

/**
 * Copy blister packs and replace cavity in coordinates, returns updated blister packs
 */
private fun List<BlisterPack>.update(
    coordinates: CavityCoordinates,
    cavity: BlisterCavity
): List<BlisterPack> {
    return mapIndexed { index, blisterPack ->
        if (index == coordinates.blisterPack) {
            BlisterPack(blisterPack.rows.mapIndexed { rowIndex, blisterPackRow ->
                if (rowIndex == coordinates.rowIndex) {
                    BlisterPackRow(blisterPackRow.value.mapIndexed { cavityIndex, blisterCavity ->
                        if (cavityIndex == coordinates.cavityIndex) {
                            cavity
                        } else blisterCavity
                    })
                } else blisterPackRow
            })
        } else blisterPack
    }
}

interface MedicineDetailActions {
    /*
    Select cavity and show dialog with actions based on cavity type
     */
    fun selectCavity(coordinates: CavityCoordinates)

    /*
    Clear cavity selected by user
     */
    fun clearSelectedCavity()

    /*
    Set selected as consumed
     */
    fun consumeSelected()

    /*
   Set selected as lost
    */
    fun setLostSelected()
}
