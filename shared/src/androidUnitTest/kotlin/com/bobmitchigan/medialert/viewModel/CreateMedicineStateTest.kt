package com.bobmitchigan.medialert.viewModel

import com.bobmitchigan.medialert.MR
import com.bobmitchigan.medialert.data.BlisterPackAdapter.serialize
import com.bobmitchigan.medialert.viewModel.state.BlisterPackDimension
import com.bobmitchigan.medialert.viewModel.state.CreateMedicineState
import com.bobmitchigan.medialert.viewModel.state.InputState
import com.bobmitchigan.medialert.viewModel.state.toInputState
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import kotlin.test.assertEquals

class CreateMedicineStateTest {

    @Test
    fun `serialize a deserialize empty create medicine state`() {
        val serialized = serialize(CreateMedicineState())

        assertEquals(CreateMedicineState(), deserialize(serialized))
    }

    @Test
    fun `serialize a deserialize create medicine state without error`() {
        val state = CreateMedicineState(
            medicineId = 4,
            name = "3".toInputState(),
            blisterPackCount = 3.toInputState(),
            dimensions = listOf(BlisterPackDimension(3.toInputState(), 6.toInputState())),
            timesPerDay = 4.toInputState(),
            timeSchedule = listOf(LocalTime(3, 4).toString().toInputState())
        )
        val serialized = serialize(state)

        assertEquals(state, deserialize(serialized))
    }

    @Test
    fun `serialize a deserialize create medicine state with transient error`() {
        val state = CreateMedicineState(
            name = "3".toInputState(),
            blisterPackCount = InputState(error = MR.strings.create_medicine_mandatory_field),
            dimensions = listOf(BlisterPackDimension(3.toInputState(), 6.toInputState()))
        )
        val serialized = serialize(state)

        assertEquals(state.copy(blisterPackCount = InputState()), deserialize(serialized))
    }

    @Test
    fun `create medicine from state`() {
        val time = LocalTime(3, 4)
        val state = CreateMedicineState(
            medicineId = 4,
            name = "Name".toInputState(),
            blisterPackCount = 3.toInputState(),
            dimensions = listOf(BlisterPackDimension(3.toInputState(), 6.toInputState())),
            timesPerDay = 4.toInputState(),
            timeSchedule = listOf(time.toString().toInputState())
        )

        val medicine = state.toMedicine(object : Clock {
            override fun now(): Instant {
                return Instant.parse(INSTANT)
            }
        })!!

        assertEquals(time, medicine.schedule.first())
        assertEquals(4, medicine.id)
        assertEquals("Name", medicine.name)
        assertEquals(
            "F.F.F.F.F.F,F.F.F.F.F.F,F.F.F.F.F.F;F.F.F.F.F.F,F.F.F.F.F.F,F.F.F" +
                    ".F.F.F;F.F.F.F.F.F,F.F.F.F.F.F,F.F.F.F.F.F",
            medicine.blisterPacks.serialize()
        )
    }

    private fun serialize(obj: Any): ByteArray {
        return ByteArrayOutputStream().apply {
            ObjectOutputStream(this).writeObject(obj)
        }.toByteArray()
    }

    private fun deserialize(bytes: ByteArray): Any? {
        return ByteArrayInputStream(bytes).run {
            ObjectInputStream(this).readObject()
        }
    }

    private companion object {
        const val INSTANT = "2022-04-03T03:06:00Z"
    }
}