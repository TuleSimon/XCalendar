package com.anonymous.xlinearcalendar.grid.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * A composable that displays a single **month cell** within a calendar grid.
 *
 * This component shows the abbreviated month name (e.g., "Jan", "Feb") and supports
 * selection styling when the month is active. It is typically used in grid layouts
 * such as a year view or month selector.
 *
 * ### Example:
 * ```kotlin
 * DefaultGridMonthCell(
 *     month = Date(),
 *     isSelected = true,
 *     onMonthSelected = { selectedMonth ->
 *         println("Selected month: $selectedMonth")
 *     }
 * )
 * ```
 *
 * @param modifier The [Modifier] to be applied to this month cell (e.g., for padding or layout adjustments).
 * @param month The [Date] object representing the month to display. Only the month and year parts are used.
 * @param isSelected Whether this month is currently selected. When `true`, the cell is visually highlighted.
 * @param onMonthSelected Callback triggered when the user taps the cell. The selected [Date] is passed as an argument.
 */
@Composable
fun DefaultGridMonthCell(
    modifier: Modifier = Modifier,
    month: Date,
    isSelected: Boolean = false,
    onMonthSelected: (Date) -> Unit
) {
    // Remembered date formatter to avoid recreation on recomposition
    val monthFormat = remember { SimpleDateFormat("MMM yyyy", Locale.getDefault()) }

    Column {

        Text(
            text = monthFormat.format(month), // e.g., "Jan", "Feb"
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primary
                    else Color.Transparent
                )
                .clickable { onMonthSelected(month) }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
        )
    }
}
