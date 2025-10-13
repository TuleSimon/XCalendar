package com.anonymous.xlinearcalendar.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anonymous.xlinearcalendar.extensions.isBefore
import com.anonymous.xlinearcalendar.extensions.now
import com.anonymous.xlinearcalendar.toDate
import com.anonymous.xlinearcalendar.toLocalDate
import kotlinx.datetime.LocalDate
import java.util.Date

/**
 * View that represents one day in the calendar.
 * @param modifier view modifier.
 * @param dateToUse the dateToUse to be displayed in the cell.
 * @param isSelected whether the view is selected. Default is false.
 * @param onDateSelected callback triggered when the cell is clicked, returning the selected [LocalDate].
 * @param shape defines the shape of this card's container, border, and shadow.
 * @param colors DateCellColors to resolve the colors used in different states of the card.
 * @param elevation DateCellElevation to resolve the shadow size and primary color overlay.
 * @param border optional DateCellBorder to draw around the container of the card.
 */
@Composable
fun DateCell(
    modifier: Modifier = Modifier,
    date: Date,
    isSelected: Boolean = false,
    onDateSelected: (Date) -> Unit,
    shape: Shape = DateCellDefaults.shape,
    colors: DateCellDefaults.DateCellColors = DateCellDefaults.colors(),
    elevation: DateCellDefaults.DateCellElevation = DateCellDefaults.elevation(),
    border: DateCellDefaults.DateCellBorder? = null
) {
    val dateToUse = remember {
        date.toLocalDate()
    }
    val cellColor = when {
        isSelected -> colors.selectedContainerColor
        dateToUse.isBefore(LocalDate.now()) -> colors.pastContainerColor
        else -> colors.futureContainerColor
    }

    val textColor = when {
        isSelected -> colors.selectedTextColor
        dateToUse.isBefore(LocalDate.now()) -> colors.pastTextColor
        else -> colors.futureTextColor
    }

    val cellBorder = border?.let {
        when {
            isSelected -> BorderStroke(it.selectedBorderWidth, it.selectedBorderColor)
            dateToUse.isBefore(LocalDate.now()) -> BorderStroke(it.pastBorderWidth, it.pastBorderColor)
            else -> BorderStroke(it.futureBorderWidth, it.futureBorderColor)
        }
    }

    val cellElevation = when {
        isSelected -> elevation.selectedElevation
        dateToUse.isBefore(LocalDate.now()) -> elevation.pastElevation
        else -> elevation.futureElevation
    }

    Card(
        modifier = modifier
            .shadow(elevation = cellElevation, shape = shape)
            .clip(shape = shape)
            .clickable { onDateSelected(dateToUse.toDate()) },
        border = cellBorder,
        colors = CardDefaults.cardColors(containerColor = cellColor),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = dateToUse.dayOfWeek.name.subSequence(0, 3).toString()
                    .lowercase()
                    .replaceFirstChar { it.uppercase() },
                color = textColor,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = dateToUse.dayOfMonth.toString(),
                fontWeight = FontWeight.Black,
                color = textColor,
                fontSize = 24.sp
            )
        }
    }
}