package com.anonymous.xlinearcalendar

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anonymous.xlinearcalendar.utils.XLinearCalendarState
import com.anonymous.xlinearcalendar.utils.rememberXLinearCalendarState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import java.util.Calendar
import java.util.Date
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant

/**
 * A horizontally scrollable calendar component built using Jetpack Compose.
 *
 * The calendar displays a list of dates that can scroll infinitely (unless bounded),
 * and provides full control through an external [XLinearCalendarState].
 *
 * You can also programmatically:
 * - Scroll to specific dates.
 * - Load previous or upcoming dates.
 * - Reinitialize the calendar with custom date ranges.
 *
 * ---
 *
 * ### Example Usage
 * ```kotlin
 * val calendarState = rememberXLinearCalendarState()
 *
 * XLinearCalendar(
 *     state = calendarState,
 *     maxDays = 180,
 *     content = { date, isSelected, onClick ->
 *         DayItem(date = date, isSelected = isSelected, onClick = onClick)
 *     }
 * )
 *
 * // Example external control
 * LaunchedEffect(Unit) {
 *     calendarState.scrollToDate(Date())
 * }
 * ```
 *
 * ---
 *
 * @param modifier Optional [Modifier] applied to the [LazyRow].
 * @param isBounded Whether the calendar should stop loading more dates when it reaches [maxDays].
 * If false, it will continuously load dates when scrolled to the edges.
 * @param state A controller instance created via [rememberXLinearCalendarState] to interact
 * with the calendar programmatically (e.g., reload, scroll, select date).
 * @param lazyListState Optional [LazyListState] for manual scroll state control.
 * If not provided, the composable creates and manages its own.
 * @param initialFirstVisibleItemIndex Optional lambda that defines the initial visible item index
 * based on the initial date list. Returns an [Int] index or `null` to center the view.
 * @param maxDays The maximum number of days to load on each side of the initial date range.
 * Defaults to 365 (one year forward/backward).
 * @param shouldLoadPrevious Whether to automatically load previous (past) dates when scrolled
 * to the start of the list. Defaults to `true`.
 * @param shouldLoadNext Whether to automatically load future (upcoming) dates when scrolled
 * to the end of the list. Defaults to `true`.
 * @param initialDates An optional list of [Date] objects to initialize the calendar with.
 * If null, the calendar will start centered around the current date.
 * @param content A composable lambda that defines how each date item is displayed.
 * It provides the [Date], a boolean flag indicating selection state, and an [onClick] callback.
 */
@Composable
fun XLinearCalendar(
    modifier: Modifier = Modifier,
    isBounded: Boolean = true,
    state: XLinearCalendarState = rememberXLinearCalendarState(),
    lazyListState: LazyListState? = null,
    initialFirstVisibleItemIndex: ((List<Date>) -> Int)? = null,
    maxDays: Int = 365,
    shouldLoadPrevious: Boolean = true,
    shouldLoadNext: Boolean = true,
    initialDates: List<Date>? = null,
    content: @Composable (date: Date, isSelected: Boolean, onClick: (Date) -> Unit) -> Unit
) {
    val uiState = state.viewModel.uiState.collectAsStateWithLifecycle().value

    val viewModel = state.viewModel

    val startIndex = initialFirstVisibleItemIndex?.invoke(uiState.dates.map { it.toDate() })
        ?: ((uiState.dates.size / 2) - 1).coerceAtLeast(0)

    val scrollState = lazyListState ?: rememberLazyListState(
        initialFirstVisibleItemIndex = startIndex,
        initialFirstVisibleItemScrollOffset = -10
    )

    LaunchedEffect(true) {
        viewModel.setMaxDaysToLoad(maxDays)
        viewModel.setIsBounded(isBounded)

        val localDates = initialDates?.map { it.toLocalDate() }
        viewModel.loadInitialDates(localDates)
    }

    LazyRow(
        state = scrollState,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        itemsIndexed(uiState.dates) { index, date ->

            if (index == 0 && !uiState.isLoading && shouldLoadPrevious) {
                viewModel.loadPreviousDates()
            } else if (index == uiState.dates.size - 1 && !uiState.isLoading && shouldLoadNext) {
                viewModel.loadUpcomingDates()
            }

            val isSelected = date == uiState.selectedDate
            content(date.toDate(), isSelected) {
                viewModel.selectDate(date)
            }

        }
    }
}


/**
 * Converts a Java [Date] to a Kotlin [LocalDate], with timezone awareness.
 *
 * @param timeZone The time zone to consider during conversion. Defaults to the system time zone.
 * @return A [LocalDate] representing the same calendar day as the given [Date].
 */
@OptIn(ExperimentalTime::class)
internal fun Date.toLocalDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.toInstant()
            .toKotlinInstant()
            .toLocalDateTime(timeZone)
            .date
    } else {
        val calendar = Calendar.getInstance().apply { time = this@toLocalDate }
        LocalDate(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH) + 1,
            day = calendar.get(Calendar.DAY_OF_MONTH)
        )
    }
}


/**
 * Converts a Kotlin [LocalDate] to a Java [Date], representing midnight of that day.
 *
 * @param timeZone The time zone to use for conversion. Defaults to the system time zone.
 * @return A [Date] instance corresponding to midnight at the start of the given [LocalDate].
 */
@OptIn(ExperimentalTime::class)
internal fun LocalDate.toDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): Date {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val instant = this
            .atStartOfDayIn(timeZone)
            .toJavaInstant()
        Date.from(instant)
    } else {
        // Fallback for Android below API 26
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, monthNumber - 1) // Calendar months are 0-based
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        calendar.time
    }
}