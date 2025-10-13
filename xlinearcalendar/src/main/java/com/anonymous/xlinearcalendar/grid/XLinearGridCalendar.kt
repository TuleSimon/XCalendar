package com.anonymous.xlinearcalendar.grid

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anonymous.xlinearcalendar.grid.components.MonthGrid
import com.anonymous.xlinearcalendar.grid.components.MonthSelectorRow
import com.anonymous.xlinearcalendar.grid.controller.XCalendarController
import com.anonymous.xlinearcalendar.grid.controller.rememberXCalendarController
import kotlinx.coroutines.launch
import java.util.Date

/**
 * ### XLinearGridCalendar
 *
 * A customizable grid-based calendar composable that allows users to:
 * - Scroll through months horizontally.
 * - Display days in a grid for the selected month.
 * - Customize the UI for both month cells and day cells via composable lambdas.
 *
 * This composable also provides automatic loading of previous and upcoming months,
 * and optional automatic scrolling to a selected month.
 *
 * ---
 *
 * #### Parameters
 * @param modifier The [Modifier] to be applied to the calendar layout.
 * @param startMonth The first month to display in the calendar. Defaults to the current date.
 * @param endMonth Optional last month to display. If null, the calendar can load months indefinitely (subject to [maxMonths] limit).
 * @param shouldLoadNext Whether to automatically load upcoming months when scrolling forward. Defaults to true.
 * @param shouldLoadPrevious Whether to automatically load previous months when scrolling backward. Defaults to true.
 * @param shouldAutoScroll Whether the calendar should automatically scroll to the selected month when it changes. Defaults to true.
 * @param maxMonths The maximum number of months to keep in memory and display. Defaults to 24.
 * @param controller The [XCalendarController] instance used to programmatically control scrolling, selection, and month/day updates.
 * @param monthContent A composable lambda to render each month cell. It provides:
 *   - `month`: The [Date] representing the month.
 *   - `isSelected`: Boolean indicating if this month is currently selected.
 *   - `onClick`: Callback invoked when the month is clicked.
 * @param dayContent A composable lambda to render each day cell. It provides:
 *   - `day`: The [Date] representing the day, or `null` for empty placeholders.
 *   - `isSelected`: Boolean indicating if this day is currently selected.
 *   - `onClick`: Callback invoked when the day is clicked.
 *
 * ---
 *
 * #### Example Usage
 * ```kotlin
 * XLinearGridCalendar(
 *     startMonth = Date(),
 *     monthContent = { month, isSelected, onClick ->
 *         DefaultGridMonthCell(month = month, isSelected = isSelected, onMonthSelected = onClick)
 *     },
 *     dayContent = { day, isSelected, onClick ->
 *         DefaultGridDayCell(date = day, selectedDate = selectedDate, onDateSelected = onClick)
 *     }
 * )
 * ```
 *
 * This design allows complete customization of both month and day UI while keeping
 * the scrolling, selection, and date management logic encapsulated in [XCalendarController].
 */
@Composable
fun XLinearGridCalendar(
    modifier: Modifier = Modifier,
    startMonth: Date = Date(),
    endMonth: Date? = null,
    shouldLoadNext: Boolean = true,
    shouldLoadPrevious: Boolean = true,
    shouldAutoScroll: Boolean = true,
    maxMonths: Int = 24,
    controller: XCalendarController = rememberXCalendarController(
        startMonth,
        endMonth,
        shouldLoadNext,
        shouldLoadPrevious,
        maxMonths
    ),
    monthContent: @Composable (month: Date, isSelected: Boolean, onClick: (Date) -> Unit) -> Unit,
    dayContent: @Composable (day: Date?, isSelected: Boolean, onClick: (Date) -> Unit) -> Unit

) {
    val months = controller.months
    var selectedMonth by remember { mutableStateOf(months.firstOrNull() ?: Date()) }
    val selectedDate by controller.selectedDate

    val scope = rememberCoroutineScope()
    Column(modifier) {

        MonthSelectorRow(
            months = months,
            state = controller.listState,
            selectedMonth = selectedMonth,
            onLoadPrevious = { controller.loadPreviousMonths() },
            onLoadNext = { controller.loadNextMonths() },
            monthContent = monthContent,
            onMonthSelected = {
                selectedMonth = it
                if (shouldAutoScroll) {
                    scope.launch {
                        controller.scrollToMonth(it)
                    }
                }
            }
        )

        Spacer(Modifier.height(12.dp))

        MonthGrid(
            month = selectedMonth,
            dayContent = dayContent,
            selectedDate = selectedDate,
            onDateSelected = {
                controller.selectDate(it)
            }
        )
    }
}
