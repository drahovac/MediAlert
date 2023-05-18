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
            .padding(start = 16.dp, top = 16.dp)
    ) {
        val verticalScroll = rememberScrollState()

        Text(
            modifier = Modifier.padding(start = 48.dp),
            style = Typography.h5,
            text = state.startingWeekDay.month.getDisplayName(TextStyle.FULL, getLocale())
        )
        CellsContent(verticalScroll)
    }
}

@Composable
private fun CellsContent(verticalScroll: ScrollState) {
    Row {
        TimeLabelColumn(verticalScroll)
        HorizontalPager(
            pageCount = 10000,
            state = rememberPagerState(initialPage = 5000)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
            ) {
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

private fun DrawScope.drawGridLines(color: Color) {
    drawHorizontalLines(color)
    drawVerticalLines(color)
}

private fun DrawScope.drawHorizontalLines(color: Color) {
    val startingHeight = 50.dp.toPx()
    val lineHeight = 72.dp.toPx()
    val lineCount = LINE_COUNT
    for (i in 0..lineCount) {
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

private const val LINE_COUNT = 23

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
