package com.anonymous.xlinearcalendar.grid.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anonymous.xlinearcalendar.components.DateCellDefaults
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ### DefaultGridDayCell
 *
 * A default implementation of a single day cell used inside the grid-based calendar.
 *
 * This composable is responsible for displaying an individual date, handling its
 * visual appearance based on whether it is selected, past, or future relative to
 * the current date, and invoking a callback when selected.
 *
 * ---
 *
 * #### Behavior
 * - Uses the color, border, and elevation configurations from [DateCellDefaults].
 * - Displays a circular shape by default (customizable via [shape]).
 * - Invokes [onDateSelected] when clicked, if [isDateEnabled] returns `true`.
 * - Automatically determines the correct color state:
 *   - **Selected** → Uses `selectedContainerColor` and `selectedTextColor`.
 *   - **Past** → Uses `pastContainerColor` and `pastTextColor`.
 *   - **Future** → Uses `futureContainerColor` and `futureTextColor`.
 *
 * ---
 *
 * #### Parameters
 * @param modifier Modifier to be applied to the cell layout.
 * @param shape The shape of the cell container (default: circular).
 * @param colors Provides container and text colors for different states.
 * @param elevation Defines elevation levels for each date state.
 * @param border Optional border styling for selected/past/future states.
 * @param date The [Date] represented by this cell. If `null`, a placeholder (empty box) is shown.
 * @param selectedDate The currently selected [Date].
 * @param isSelected Whether this date cell is selected. Calculated automatically if not specified.
 * @param isDateEnabled Lambda determining if a date is selectable.
 * @param onDateSelected Callback invoked when a date is selected.
 */
@Composable
fun DefaultGridDayCell(
    modifier: Modifier = Modifier,
    date: Date?,
    shape: Shape = DateCellDefaults.shape,
    colors: DateCellDefaults.DateCellColors = DateCellDefaults.colors(),
    elevation: DateCellDefaults.DateCellElevation = DateCellDefaults.elevation(),
    border: DateCellDefaults.DateCellBorder? = null,
    isSelected: Boolean = false,
    isDateEnabled: (Date) -> Boolean = { true },
    onDateSelected: (Date) -> Unit,
) {
    if (date == null) {
        Spacer(Modifier.aspectRatio(1f))
        return
    }

    val today = remember { Date() }

    val isSelectedState = remember(isSelected) {
        isSelected
    }


    val isPast = date.before(today) && !isSelectedState
    val enabled = isDateEnabled(date)

    val (containerColor, textColor, elevationDp, borderStroke) = when {
        isSelectedState -> Quad(
            colors.selectedContainerColor,
            colors.selectedTextColor,
            elevation.selectedElevation,
            border?.let {
                BorderStroke(it.selectedBorderWidth, it.selectedBorderColor)
            }
        )

        isPast -> Quad(
            colors.pastContainerColor,
            colors.pastTextColor,
            elevation.pastElevation,
            border?.let {
                BorderStroke(it.pastBorderWidth, it.pastBorderColor)
            }
        )

        else -> Quad(
            colors.futureContainerColor,
            colors.futureTextColor,
            elevation.futureElevation,
            border?.let {
                BorderStroke(it.futureBorderWidth, it.futureBorderColor)
            }
        )
    }

    Card(
        modifier = modifier
            .clip(shape)
            .clickable(enabled) { onDateSelected(date) },
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevationDp),
        border = borderStroke,
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(containerColor)
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = SimpleDateFormat("EEE", Locale.getDefault()).format(date),
                    fontSize = 12.sp,
                    color = if (!enabled) textColor.copy(alpha = 0.4f) else textColor,
                    fontWeight = if (isSelectedState) FontWeight.Bold else FontWeight.Normal
                )
                Text(
                    text = SimpleDateFormat("d", Locale.getDefault()).format(date),
                    color = if (!enabled) textColor.copy(alpha = 0.4f) else textColor,
                    fontWeight = if (isSelectedState) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Small helper tuple-like class to destructure multiple properties cleanly.
 */
private data class Quad<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D?
)
