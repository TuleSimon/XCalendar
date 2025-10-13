package com.anonymous.xlinearcalendar.grid.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * A horizontally scrollable month selector used for switching between months in a calendar.
 *
 * This component displays a row of months (e.g., *Jan, Feb, Mar, ...*) and allows the user
 * to select one. You can fully customize how each month cell is displayed using the [monthContent] composable.
 *
 * It also supports dynamic loading of additional months when the user scrolls
 * to either end of the list via the [onLoadPrevious] and [onLoadNext] callbacks.
 *
 * ---
 * ### Example Usage:
 * ```kotlin
 * val months = remember { generatePastAndFutureMonths() }
 *
 * MonthSelectorRow(
 *     months = months,
 *     selectedMonth = Date(),
 *     onMonthSelected = { selected ->
 *         println("Selected month: $selected")
 *     },
 *     monthContent = { month, isSelected, onClick ->
 *         DefaultGridMonthCell(
 *             month = month,
 *             isSelected = isSelected,
 *             onMonthSelected = onClick
 *         )
 *     }
 * )
 * ```
 * ---
 *
 * @param months A list of [Date] objects representing the available months to display.
 * @param selectedMonth The currently selected month in the list.
 * @param monthContent A composable used to display each month cell.
 * Provides:
 *  - `month`: The month [Date] to display.
 *  - `isSelected`: Whether this month is currently selected.
 *  - `onClick`: A callback to trigger when the month is selected.
 * @param onLoadPrevious Optional callback triggered when the first visible item is reached,
 * allowing you to load additional previous months dynamically.
 * @param onLoadNext Optional callback triggered when the last visible item is reached,
 * allowing you to load additional future months dynamically.
 * @param onMonthSelected Callback invoked when a month is selected.
 */
@Composable
internal fun MonthSelectorRow(
    months: List<Date>,
    selectedMonth: Date,
    state: LazyListState,
    monthContent: @Composable (month: Date, isSelected: Boolean, onClick: (Date) -> Unit) -> Unit,
    onLoadPrevious: (() -> Unit)? = null,
    onLoadNext: (() -> Unit)? = null,
    onMonthSelected: (Date) -> Unit
) {

    LazyRow(
        state = state,
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        itemsIndexed(months) { index, month ->

            // === Boundary checks, same logic as your linear calendar ===
            if (index == 0 && onLoadPrevious != null) {
                onLoadPrevious()
            } else if (index == months.size - 1 && onLoadNext != null) {
                onLoadNext()
            }
            val isSelected = remember(selectedMonth) {
                selectedMonth.time==month.time
            }
            monthContent(month, isSelected) {
                onMonthSelected(month)
            }

        }
    }
}
