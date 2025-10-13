package com.anonymous.xlinearcalendar.grid.controller

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.android.awaitFrame
import java.util.*

/**
 * Controller class responsible for managing the state of the grid-style calendar.
 * It holds the list of visible months, manages selection, and dynamically loads
 * more months when needed.
 *
 * @param startMonth The starting month of the calendar (defaults to current month - 12).
 * @param endMonth The ending month of the calendar (defaults to current month + 12).
 * @param maxMonths The maximum number of months to load into memory.
 * @param loadNext Whether loading of future months is allowed when scrolled to the end.
 * @param loadPrevious Whether loading of past months is allowed when scrolled to the beginning.
 * @param listState Optional [LazyListState] for external scroll control of the month row.
 */
class XCalendarController internal constructor(
    startMonth: Date? = null,
    endMonth: Date? = null,
    private val maxMonths: Int = 120,
    private val loadNext: Boolean = true,
    private val loadPrevious: Boolean = true,
    internal val listState: LazyListState = LazyListState()
) {
    private val _months = mutableStateListOf<Date>()
    val months: List<Date> get() = _months

    var loading: Boolean = false
        private set

    private val _selectedDate = mutableStateOf<Date?>(null)
    val selectedDate: State<Date?> get() = _selectedDate

    init {
        val current = getMonthStart(Date())
        val start = startMonth?.let { getMonthStart(it) } ?: current.applyMonthOffset(-12)
        val end =
            endMonth?.let { getMonthEnd(it) } ?: current.applyMonthOffset(12, endOfMonth = true)
        generateMonths(start, end)
    }

    private fun generateMonths(start: Date, end: Date) {
        loading = true
        val cal = Calendar.getInstance().apply { time = start }
        while (!cal.time.after(end) && _months.size < maxMonths) {
            _months.add(cal.time)
            cal.add(Calendar.MONTH, 1)
        }
        loading = false
    }

    fun selectDate(date: Date) {
        _selectedDate.value = date
    }

    /**
     * Smoothly scrolls to the month containing the given date, if available.
     */
    internal suspend fun scrollToMonth(month: Date) {
        val index = _months.indexOfFirst { it.sameMonth(month) }
        if (index != -1) {
            listState.scrollItemToCenter(index)
        }
    }

    suspend fun LazyListState.scrollItemToCenter(index: Int) {
        val viewportCenter = layoutInfo.viewportEndOffset / 2
        val visibleItem = layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }

        if (visibleItem != null) {

            val itemCenter = visibleItem.offset + visibleItem.size / 2

            // Offset needed to bring itemCenter to viewportCenter
            val scrollOffset = itemCenter - viewportCenter

            // Scroll to the index with calculated offset
            animateScrollToItem(index, if(scrollOffset>=0)-scrollOffset else scrollOffset)

        } else {
            // If item is not visible, just scroll to it normally (will be adjusted next frame)
            animateScrollToItem(index)
            // small delay to wait for recomposition/layout
            // then recalc to center properly
            awaitFrame()
            scrollItemToCenter(index)
        }
    }
    fun loadNextMonths(count: Int = 12) {
        if (!loadNext || loading) return
        loading = true
        val last = _months.lastOrNull() ?: return
        val cal = Calendar.getInstance().apply { time = last }
        repeat(count) {
            cal.add(Calendar.MONTH, 1)
            if (_months.size < maxMonths) _months.add(cal.time)
        }
        loading = false
    }

    fun loadPreviousMonths(count: Int = 12) {
        if (!loadPrevious || loading) return
        loading = true
        val first = _months.firstOrNull() ?: return
        val cal = Calendar.getInstance().apply { time = first }
        repeat(count) {
            cal.add(Calendar.MONTH, -1)
            if (_months.size < maxMonths) _months.add(0, cal.time)
        }
        loading = false
    }

    fun reloadRange(newStart: Date, newEnd: Date) {
        _months.clear()
        generateMonths(getMonthStart(newStart), getMonthEnd(newEnd))
    }

    private fun getMonthStart(date: Date): Date {
        val cal = Calendar.getInstance().apply {
            time = date
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.time
    }

    private fun getMonthEnd(date: Date): Date {
        val cal = Calendar.getInstance().apply {
            time = date
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return cal.time
    }

    private fun Date.applyMonthOffset(offset: Int, endOfMonth: Boolean = false): Date {
        val cal = Calendar.getInstance().apply { time = this@applyMonthOffset }
        cal.add(Calendar.MONTH, offset)
        if (endOfMonth) {
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
            cal.set(Calendar.HOUR_OF_DAY, 23)
            cal.set(Calendar.MINUTE, 59)
            cal.set(Calendar.SECOND, 59)
            cal.set(Calendar.MILLISECOND, 999)
        } else {
            cal.set(Calendar.DAY_OF_MONTH, 1)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
        }
        return cal.time
    }

    private fun Date.sameMonth(other: Date): Boolean {
        val c1 = Calendar.getInstance().apply { time = this@sameMonth }
        val c2 = Calendar.getInstance().apply { time = other }
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
    }
}

/**
 * Remembers and creates an [XCalendarController] instance, optionally allowing
 * users to provide their own [LazyListState] for month scroll control.
 */
@Composable
fun rememberXCalendarController(
    startMonth: Date? = null,
    endMonth: Date? = null,
    loadNext: Boolean = true,
    loadPrevious: Boolean = true,
    maxMonths: Int = 120,
    listState: LazyListState = rememberLazyListState(),
): XCalendarController {
    return remember {
        XCalendarController(startMonth, endMonth, maxMonths, loadNext, loadPrevious, listState)
    }
}
