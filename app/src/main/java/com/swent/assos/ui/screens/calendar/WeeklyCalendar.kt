package com.swent.assos.ui.screens.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.swent.assos.model.data.CalendarUiModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val DayFormatter = DateTimeFormatter.ofPattern("EE, MMM d")

@Composable
fun ChangeWeek(
    data: CalendarUiModel,
    onPrevClickListener: (LocalDate) -> Unit,
    onNextClickListener: (LocalDate) -> Unit,
) {
  Row {
    IconButton(onClick = { onPrevClickListener(data.startDate.date) }) {
      Icon(imageVector = Icons.Filled.ChevronLeft, contentDescription = "Back")
    }
    IconButton(onClick = { onNextClickListener(data.endDate.date) }) {
      Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = "Next")
    }
  }
}

@Composable
fun WeekHeader(
    data: CalendarUiModel,
    dayWidth: Dp,
    modifier: Modifier,
    dayHeader: @Composable (day: LocalDate) -> Unit = { WeekDayHeader(day = it) },
) {
  Row(modifier = modifier) {
    val numDays = 7
    repeat(numDays) { i ->
      val date = data.visibleDates[i]
      Box(modifier = Modifier.width(dayWidth)) { dayHeader(date.date) }
    }
  }
}

@Composable
fun WeekDayHeader(
    day: LocalDate,
    modifier: Modifier = Modifier,
) {
  Text(
      text = day.format(DayFormatter),
      textAlign = TextAlign.Center,
      modifier = modifier.fillMaxWidth().padding(4.dp))
}
