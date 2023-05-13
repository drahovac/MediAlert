package com.bobmitchigan.medialert.android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.bobmitchigan.medialert.viewModel.state.CalendarState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.koin.androidx.compose.getViewModel
import java.time.format.TextStyle
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
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            style = Typography.h4,
            text = state.startingWeekDay.month.getDisplayName(TextStyle.FULL, getLocale())
        )
    }
}

@Composable
@ReadOnlyComposable
fun getLocale(): Locale {
    val configuration = LocalConfiguration.current
    return ConfigurationCompat.getLocales(configuration).get(0)
        ?: LocaleListCompat.getDefault()[0]!!
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
