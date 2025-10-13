package com.simon.xlinearcalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anonymous.xlinearcalendar.XLinearCalendar
import com.anonymous.xlinearcalendar.components.DateCell
import com.anonymous.xlinearcalendar.grid.XLinearGridCalendar
import com.anonymous.xlinearcalendar.grid.components.DefaultGridDayCell
import com.anonymous.xlinearcalendar.grid.components.DefaultGridMonthCell
import com.anonymous.xlinearcalendar.utils.rememberXLinearCalendarState
import com.simon.xlinearcalendar.ui.theme.XLinearCalendarTheme
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XLinearCalendarTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                ) { innerPadding ->
                    Sample()
                }
            }
        }
    }
}

@Composable
fun Sample() {
    val customDates = listOf(
        Date(2025 - 1900, 9, 10), // Oct 10, 2025
        Date(2025 - 1900, 9, 11),
        Date(2025 - 1900, 9, 12)
    )
    Column {
        XLinearCalendar(
            modifier = Modifier.height(100.dp),
            initialDates = customDates,
            shouldLoadNext = false,
            shouldLoadPrevious = false,
            content = { date, isSelected, onClick ->
                DateCell(
                    date = date,
                    isSelected = isSelected,
                    onDateSelected = onClick,
                    modifier = Modifier.padding(6.dp)
                )

            }
        )
        BoundedCalendarExample()
        XLinearGridCalendar(
            modifier = Modifier,
            shouldLoadNext = true,
            shouldLoadPrevious = true,
            monthContent = { date, isSelected, onClick ->
                DefaultGridMonthCell(
                    month = date,
                    isSelected = isSelected,
                    onMonthSelected = onClick,
                    modifier = Modifier.padding(6.dp)
                )
            },
            dayContent = { date, isSelected, onClick ->
                DefaultGridDayCell(
                    date = date,
                    isSelected = isSelected,
                    onDateSelected = onClick,
                    modifier = Modifier.padding(6.dp)
                )
            },
        )
    }
}

/** 4. Bounded Calendar with Limits */
@Composable
fun BoundedCalendarExample() {
    XLinearCalendar(
        isBounded = true,
        maxDays = 90,
        shouldLoadNext = false,
        shouldLoadPrevious = false,
        state = rememberXLinearCalendarState(instanceName = "NewInstance"),
        content = { date, isSelected, onClick ->
            DateCell(
                date = date,
                isSelected = isSelected,
                onDateSelected = onClick,
                modifier = Modifier.padding(4.dp)
            )
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewUi() {
    XLinearCalendarTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) { _ ->
            Sample()
        }
    }
}