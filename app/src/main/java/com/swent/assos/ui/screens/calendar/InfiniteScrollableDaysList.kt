package com.swent.assos.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun InfiniteScrollableDaysList(
    selectedDate: State<LocalDate>,
    onDateSelected: (LocalDate) -> Unit
) {
  val startDate = LocalDate.now().minusDays(3L) // Start from 3 days ago

  // Generate a list of LocalDate objects representing days
  val daysList = remember {
    List(365 * 10) { // Generating 10 years worth of days
      startDate.plusDays(it.toLong())
    }
  }

  LazyRow(modifier = Modifier.testTag("DaysList")) {
    // Loop through the list of days infinitely
    items(daysList) { day ->
      when (day) {
        selectedDate.value -> DayItemSelected(day)
        else -> DayItem(date = day, onDateSelected)
      }
    }
  }
}

@Composable
fun DayItem(date: LocalDate, onDateSelected: (LocalDate) -> Unit) {
  Column(
      modifier =
          Modifier.width(53.dp)
              .height(70.dp)
              .clip(RoundedCornerShape(16.dp))
              .clickable(onClick = { onDateSelected(date) })
              .testTag("DayItem"),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
        text = date.dayOfMonth.toString(),
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground)
    Text(
        text =
            when (date.dayOfWeek) {
              DayOfWeek.MONDAY -> "Mo"
              DayOfWeek.TUESDAY -> "Tu"
              DayOfWeek.WEDNESDAY -> "Wed"
              DayOfWeek.THURSDAY -> "Th"
              DayOfWeek.FRIDAY -> "Fr"
              DayOfWeek.SATURDAY -> "Sa"
              else -> "Su"
            },
        fontSize = 12.sp,
        color = Color(0xFF94A3B8),
        fontWeight = FontWeight.Normal)
  }
}

@Composable
fun DayItemSelected(date: LocalDate) {
  Column(
      modifier =
          Modifier.width(53.dp)
              .height(70.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(Color(0xFFFFF0F0))
              .testTag("DayItemSelected"),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
        modifier = Modifier.testTag("TitleItemSelected"),
        text = date.dayOfMonth.toString(),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFDE496E))
    Text(
        text =
            when (date.dayOfWeek) {
              DayOfWeek.MONDAY -> "Mo"
              DayOfWeek.TUESDAY -> "Tu"
              DayOfWeek.WEDNESDAY -> "Wed"
              DayOfWeek.THURSDAY -> "Th"
              DayOfWeek.FRIDAY -> "Fr"
              DayOfWeek.SATURDAY -> "Sa"
              else -> "Su"
            },
        fontSize = 14.sp,
        color = Color(0xFFDE496E),
        fontWeight = FontWeight.Medium)
  }
}
