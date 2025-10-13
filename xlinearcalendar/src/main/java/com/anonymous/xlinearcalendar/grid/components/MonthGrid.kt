package com.anonymous.xlinearcalendar.grid.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.Date

/**
 * Displays a **monthly calendar grid** for a given [month].
 *
 * This composable renders a 7-column grid representing the days of a month,
 * starting from Monday by default. Empty cells are added at the beginning of the grid
 * to align the first day of the month correctly based on the weekday it falls on.
 *
 * Each day cell is built using the [dayContent] composable, which provides
 * full control over how each date is visually represented.
 *
 * ---
 * ### Example:
 * ```kotlin
 * MonthGrid(
 *     month = Date(),
 *     selectedDate = Date(),
 *     onDateSelected = { selected -> println("Selected date: $selected") },
 *     dayContent = { date, isSelected, onClick ->
 *         if (date != null) {
 *             Text(
 *                 text = SimpleDateFormat("d", Locale.getDefault()).format(date),
 *                 modifier = Modifier
 *                     .clip(RoundedCornerShape(4.dp))
 *                     .background(if (isSelected) Color.Blue else Color.Transparent)
 *                     .clickable { onClick(date) }
 *                     .padding(8.dp)
 *             )
 *         } else {
 *             Spacer(Modifier.size(32.dp)) // empty cell placeholder
 *         }
 *     }
 * )
 * ```
 * ---
 *
 * @param month The [Date] representing the month to display (the specific day is ignored).
 * @param dayContent A composable used to render each day cell.
 * Provides:
 *  - `day`: the [Date] for the cell (or `null` for empty cells before the first day of the month).
 *  - `isSelected`: whether this date matches [selectedDate].
 *  - `onClick`: callback to handle date selection.
 * @param selectedDate The currently selected date, if any.
 * @param onDateSelected Callback invoked when a date cell is clicked.
 */
@Composable
internal fun MonthGrid(
    month: Date,
    dayContent: @Composable (day: Date?, isSelected: Boolean, onClick: (Date) -> Unit) -> Unit,
    selectedDate: Date?,
    onDateSelected: (Date) -> Unit,
) {
    val days = remember(month) {
        val cal = Calendar.getInstance().apply { time = month }
        val totalDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = (cal.get(Calendar.DAY_OF_WEEK) + 6) % 7 // make Monday=0
        buildList<Date?> {
            repeat(firstDayOfWeek) { add(null) }
            for (day in 1..totalDays) {
                cal.set(Calendar.DAY_OF_MONTH, day)
                add(cal.time)
            }
        }
    }


    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(days) { date ->
            dayContent(date, date == selectedDate, onDateSelected)
        }
    }
}
