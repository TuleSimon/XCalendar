package com.anonymous.xlinearcalendar.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anonymous.xlinearcalendar.XLinearCalendarViewModel
import com.anonymous.xlinearcalendar.toLocalDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date

/**
 * A controller/state holder for [XLinearCalendar].
 *
 * This class allows you to:
 * - Programmatically load more dates (past or future)
 * - Reload the calendar with a new list of dates
 * - Scroll to a specific date
 * - Select a date manually
 *
 * You can obtain an instance using [rememberXLinearCalendarState].
 *
 * Example:
 * ```
 * val calendarState = rememberXLinearCalendarState()
 *
 * XLinearCalendar(
 *     state = calendarState,
 *     content = { date, isSelected, onClick ->
 *         // Your date item UI
 *     }
 * )
 *
 * LaunchedEffect(Unit) {
 *     calendarState.scrollToDate(Date())
 *     calendarState.loadMoreFuture()
 * }
 * ```
 *
 * @param viewModel The internal [XLinearCalendarViewModel] managing the date logic.
 * @param listState The [LazyListState] controlling the scroll position of the calendar.
 * @param scope A [CoroutineScope] used for smooth scroll animations.
 */
class XLinearCalendarState internal constructor(
    internal val viewModel: XLinearCalendarViewModel,
    internal val listState: LazyListState,
    internal val scope: CoroutineScope
) {

    /**
     * Loads more **past** dates (older than the currently visible range).
     *
     * Useful when implementing a "Load previous dates" button or manually
     * extending the calendar range backward.
     */
    fun loadMorePast() {
        viewModel.loadPreviousDates()
    }

    /**
     * Loads more **future** dates (newer than the currently visible range).
     *
     * Useful when implementing a "Load more dates" button or manually
     * extending the calendar range forward.
     */
    fun loadMoreFuture() {
        viewModel.loadUpcomingDates()
    }

    /**
     * Reloads the calendar with a **new initial list of dates**.
     *
     * This completely replaces the current date list in the ViewModel.
     * You can use it to reinitialize the calendar when switching contexts
     * (for example, after selecting a new date range).
     *
     * @param initialDates The list of [Date] objects to display initially.
     */
    fun reload(initialDates: List<Date>) {
        viewModel.loadInitialDates(initialDates.map { it.toLocalDate() })
    }

    /**
     * Smoothly scrolls the calendar to the given [targetDate],
     * if it already exists in the current date list.
     *
     * If the date is outside the currently loaded range, nothing happens.
     * To support auto-loading nearby ranges, consider checking and loading
     * more dates before calling this.
     *
     * @param targetDate The [Date] to scroll to.
     */
    fun scrollToDate(targetDate: Date) {
        val index = viewModel.uiState.value.dates.indexOfFirst {
            it == targetDate.toLocalDate()
        }
        if (index != -1) {
            scope.launch {
                listState.animateScrollToItem(index)
            }
        }
    }

    /**
     * Programmatically selects the given [date] as the active calendar selection.
     *
     * This updates the selected state but does not scroll automatically.
     * You can combine this with [scrollToDate] if you want to both select
     * and move to that date visually.
     *
     * @param date The [Date] to select.
     */
    fun selectDate(date: Date) {
        viewModel.selectDate(date.toLocalDate())
    }
}

/**
 * Remembers and returns an instance of [XLinearCalendarState] for controlling
 * an [XLinearCalendar] composable.
 *
 * This function ensures the same state instance survives recomposition.
 *
 * @param listState The [LazyListState] controlling the scroll position. Defaults to a new one.
 * @param instanceName for handling multiple calendars in one screen so states doesn't reuse
 * @return A remembered [XLinearCalendarState] that can be passed to [XLinearCalendar].
 */
@Composable
fun rememberXLinearCalendarState(
    listState: LazyListState = rememberLazyListState(),
    instanceName:String = "defaultInstanceName"
): XLinearCalendarState {
    val scope = rememberCoroutineScope()
    val viewModel: XLinearCalendarViewModel = viewModel(key = instanceName) { XLinearCalendarViewModel() }
    return remember(viewModel, listState, scope) {
        XLinearCalendarState(viewModel, listState, scope)
    }
}
