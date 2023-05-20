@file:OptIn(ExperimentalFoundationApi::class)

package com.bobmitchigan.medialert.android.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bobmitchigan.medialert.android.design.theme.Typography
import com.bobmitchigan.medialert.viewModel.CalendarViewModel
import com.bobmitchigan.medialert.viewModel.state.CalendarState
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
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
            .padding(start = 16.dp, top = 16.dp)
    ) {
        val verticalScroll = rememberScrollState()
        val pagerState = rememberPagerState(initialPage = PAGE_OFFSET)
        val currentWeek = pagerState.currentPage - PAGE_OFFSET
        val startingWeekDay: LocalDate = getFirstWeekDay(currentWeek)

        Text(
            modifier = Modifier.padding(start = 48.dp),
            style = Typography.h5,
            text = startingWeekDay.month.getDisplayName(TextStyle.FULL, getLocale())
        )
        CellsContent(pagerState, verticalScroll)
    }
}

@Composable
private fun CellsContent(
    pagerState: PagerState,
    verticalScroll: ScrollState
) {
    Row {
        TimeLabelColumn(verticalScroll)
        HorizontalPager(
            pageCount = 10000,
            state = pagerState,
        ) {
            val firstWeekDay = getFirstWeekDay(it - PAGE_OFFSET)
            Column(
                Modifier
                    .fillMaxWidth()
            ) {
                ShortWeekDays()
                GetDays(firstWeekDay)

                val color = MaterialTheme.colors.onBackground
                Column(
                    Modifier
                        .verticalScroll(verticalScroll)
                        .drawBehind {
                            drawGridLines(color)
                        }) {
                    for (i in 0..96) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                        ) {
                            // TODO
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GetDays(firstWeekDay: LocalDate) {
    Row {
        for (day in 0..6) {
            Text(
                modifier = Modifier.weight(1f),
                text = firstWeekDay.plus(day, DateTimeUnit.DAY).dayOfMonth.toString(),
                style = Typography.subtitle1,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ShortWeekDays() {
    Row {
        getShortWeekDays().forEach {
            Text(
                modifier = Modifier.weight(1f),
                text = it,
                style = Typography.subtitle1,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun DrawScope.drawGridLines(color: Color) {
    drawHorizontalLines(color)
    drawVerticalLines(color)
}

private fun DrawScope.drawHorizontalLines(color: Color) {
    val startingHeight = 50.dp.toPx()
    val lineHeight = 20.6.dp.toPx()
    for (i in 0..LINE_COUNT) {
        drawLine(
            color = color,
            start = Offset(0f, startingHeight + (i * lineHeight).dp.toPx()),
            end = Offset(size.width, startingHeight + (i * lineHeight).dp.toPx())
        )
    }
}

@Suppress("MagicNumber")
private fun DrawScope.drawVerticalLines(color: Color) {
    val width = size.width / 7
    for (i in 0..6) {
        drawLine(
            color = color,
            start = Offset(i * width, 0f),
            end = Offset(i * width, size.height)
        )
    }
}

@Composable
private fun TimeLabelColumn(verticalScroll: ScrollState) {
    Column(
        Modifier
            .padding(top = 24.dp)
            .width(48.dp)
            .verticalScroll(verticalScroll)
    ) {
        for (i in 1..23) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .height(48.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "$i:00"
                )
            }
        }
    }
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
    firstDayIsoNumber().let { firstDay ->
        val shortWeekdays = DateFormatSymbols().shortWeekdays
        val adjustedShortWeekdays = mutableListOf<String>()
        for (i in 0..6) {
            adjustedShortWeekdays.add(shortWeekdays[getFirstDayOffset(i, firstDay)])
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

private fun getFirstDayOffset(currentDay: Int, firstDay: Int) =
    ((currentDay + firstDay) % DAYS_IN_WEEK) + 1

private fun firstDayIsoNumber() = WeekFields.of(Locale.getDefault()).firstDayOfWeek.isoDayNumber

/**
 * Retrieves the first day of the week based on the starting week index counted.
 *
 * @param startingWeekIndex The index of the week. Today week = 0 - future week +1, past -1.
 * @param clock (Optional) The clock used to determine the current date and time. It defaults to the system clock.
 *
 * @return The [LocalDate] representing the first day of the week.
 */
fun getFirstWeekDay(startingWeekIndex: Int, clock: Clock = Clock.System): LocalDate {
    return clock.todayIn(TimeZone.currentSystemDefault()).run {
        when {
            dayOfWeek.isoDayNumber == firstDayIsoNumber() -> this
            dayOfWeek.isoDayNumber > firstDayIsoNumber() -> minus(
                dayOfWeek.isoDayNumber - firstDayIsoNumber(),
                DateTimeUnit.DAY
            )

            else -> minus(
                dayOfWeek.isoDayNumber,
                DateTimeUnit.DAY
            ) // probably not working for different than sunday, but who cares
        }.plus(startingWeekIndex * DAYS_IN_WEEK, DateTimeUnit.DAY)
    }
}

private const val PAGE_OFFSET = 5000
private const val LINE_COUNT = 23
private const val DAYS_IN_WEEK = 7

@Preview
@Composable
fun CalendarScreenPreview() {
    MaterialTheme {
        CalendarContent(CalendarState())
    }
}
