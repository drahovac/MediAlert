@file:OptIn(ExperimentalFoundationApi::class)

package com.bobmitchigan.medialert.android.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bobmitchigan.medialert.android.design.theme.Typography
import com.bobmitchigan.medialert.viewModel.CalendarViewModel
import com.bobmitchigan.medialert.viewModel.state.CalendarCell
import com.bobmitchigan.medialert.viewModel.state.CalendarState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.isoDayNumber
import org.koin.androidx.compose.getViewModel
import java.text.DateFormatSymbols
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = getViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CalendarContent(state)
}

@Composable
private fun CalendarContent(state: CalendarState) {
    Column(
        Modifier
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier.padding(start = 48.dp),
            style = Typography.h5,
            text = state.startingWeekDay.month.getDisplayName(TextStyle.FULL, getLocale())
        )
        HorizontalPager(pageCount = 10000) {
            Grid(state.cells)
        }
    }
}

@Composable
fun Grid(cells: List<CalendarCell>) {
    LazyVerticalGrid(columns = GridCells.Fixed(8), content = {
        item {} // skip cell
        getShortWeekDays().forEach {
            item {
                Text(
                    text = it,
                    style = Typography.subtitle1
                )
            }
        }
        cells.map { cell ->
            when (cell) {
                is CalendarCell.SlotCell -> item { }
                is CalendarCell.TimeCell -> item {
                    cell.hour?.let { Text(text = "${cell.hour}:00") } ?: Spacer(
                        modifier = Modifier.height(24.dp)
                    )
                }
            }
        }
    })
}

@Composable
@ReadOnlyComposable
fun getLocale(): Locale {
    val configuration = LocalConfiguration.current
    return ConfigurationCompat.getLocales(configuration).get(0)
        ?: LocaleListCompat.getDefault()[0]!!
}

/**
 * Gets a list of short weekday names for the current locale.
 * Returns correct first weekday for default locale.
 *
 * @return A list of short weekday names for the current locale.
 */
@Suppress("MagicNumber")
fun getShortWeekDays(): List<String> {
    WeekFields.of(Locale.getDefault()).firstDayOfWeek.isoDayNumber.let { firstDay ->
        val shortWeekdays = DateFormatSymbols().shortWeekdays
        val adjustedShortWeekdays = mutableListOf<String>()
        for (i in 0..6) {
            adjustedShortWeekdays.add(shortWeekdays[((i + firstDay) % 7) + 1])
        }
        return adjustedShortWeekdays.map { weekDay ->
            weekDay.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        }
    }
}

@Preview
@Composable
fun CalendarScreenPreview() {
    MaterialTheme {
        CalendarContent(
            CalendarState(
                LocalDate(2022, 3, 3),
                LocalDateTime(2022, 3, 3, 12, 45)
            )
        )
    }
}
