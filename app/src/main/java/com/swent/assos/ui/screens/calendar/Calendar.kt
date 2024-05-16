package com.swent.assos.ui.screens.calendar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.data.Event
import com.swent.assos.model.view.CalendarViewModel
import com.swent.assos.ui.components.LoadingCircle
import com.swent.assos.ui.components.PageTitle
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dayWidth = 256.dp
private val hourHeight = 64.dp
private val dateFormatter = DateTimeFormatter.ofPattern("dd LLL uuuu")

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showSystemUi = true)
@Composable
fun Calendar(
    eventContent: @Composable (event: Event) -> Unit = { BasicEvent(event = it) },
) {
  val calendarViewModel: CalendarViewModel = hiltViewModel()
  val events = calendarViewModel.events.collectAsState()
  val selectedEvents = calendarViewModel.selectedEvents.collectAsState()
  val selectedDate = calendarViewModel.selectedDate.collectAsState()
  val loading = calendarViewModel.loading.collectAsState()

  val verticalScrollState = rememberScrollState(1350)

  LaunchedEffect(key1 = Unit) { calendarViewModel.updateEvents() }

  LaunchedEffect(events.value, selectedDate.value) { calendarViewModel.filterEvents() }

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("CalendarScreen"),
      topBar = { PageTitle(title = "Calendar - ${selectedDate.value.format(dateFormatter)}") }) {
        if (loading.value) {
          LoadingCircle()
        } else {
          Column(
              modifier =
                  Modifier.padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
                      .padding(it)
                      .fillMaxSize()) {
                InfiniteScrollableDaysList(
                    selectedDate = selectedDate,
                    onDateSelected = { newDate -> calendarViewModel.updateSelectedDate(newDate) })

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text =
                        if (selectedDate.value == LocalDate.now()) {
                          "Schedule Today"
                        } else {
                          "Daily Schedule"
                        },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E293B))

                Spacer(modifier = Modifier.height(32.dp))
                DailySchedule(
                    events = selectedEvents,
                    verticalScrollState = verticalScrollState,
                    eventContent = eventContent)
                Spacer(modifier = Modifier.height(32.dp))

                Reminder(calendarViewModel = calendarViewModel)
              }
        }
      }
}

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
    itemsIndexed(daysList) { index, day ->
      DayItem(date = day, selected = day == selectedDate.value, onDateSelected)
      if (day == selectedDate.value ||
          index < daysList.size - 1 && daysList[index + 1] == selectedDate.value) {
        // Adjust spacing between the selected data
        Spacer(modifier = Modifier.width(14.5.dp))
      } else {
        // Adjust spacing between other dates
        Spacer(modifier = Modifier.width(26.dp))
      }
    }
  }
}

@Composable
fun DayItem(date: LocalDate, selected: Boolean, onDateSelected: (LocalDate) -> Unit) {
  Surface(
      modifier =
          if (selected) {
            Modifier.width(53.dp).height(79.dp).testTag("DayItemSelected")
          } else {
            Modifier.width(32.dp)
                .height(79.dp)
                .clickable(onClick = { onDateSelected(date) })
                .testTag("DayItem")
          },
      color =
          if (selected) {
            Color(0xFFFFF0F0)
          } else {
            Color.Transparent
          },
      shape = RoundedCornerShape(16.dp)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Text(
              modifier =
                  if (selected) {
                    Modifier.testTag("TitleItemSelected")
                  } else {
                    Modifier
                  },
              text = date.dayOfMonth.toString(),
              fontSize =
                  if (selected) {
                    20.sp
                  } else {
                    18.sp
                  },
              fontWeight =
                  if (selected) {
                    FontWeight.Bold
                  } else {
                    FontWeight.SemiBold
                  },
              color = if (selected) Color(0xFFDE496E) else Color(0xFF1E293B))
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
              fontSize =
                  if (selected) {
                    14.sp
                  } else {
                    12.sp
                  },
              color = if (selected) Color(0xFFDE496E) else Color(0xFF94A3B8),
              fontWeight =
                  if (selected) {
                    FontWeight.Medium
                  } else {
                    FontWeight.Normal
                  })
          if (selected) {
            Canvas(modifier = Modifier.width(53.dp)) {
              drawCircle(
                  color = Color(0xFFDE496E),
                  radius = 8f,
                  center = Offset(size.width / 2, 8.dp.toPx()))
            }
          }
        }
      }
}

@Composable
fun DailySchedule(
    events: State<List<Event>>,
    verticalScrollState: ScrollState,
    eventContent: @Composable (event: Event) -> Unit
) {
  Row(modifier = Modifier.testTag("EventUI").height(208.dp)) {
    TimeSidebar(hourHeight = hourHeight, modifier = Modifier.verticalScroll(verticalScrollState))

    Schedule(
        events = events,
        eventContent = eventContent,
        dayWidth = dayWidth,
        hourHeight = hourHeight,
        modifier = Modifier.weight(1f).verticalScroll(verticalScrollState))
  }
}
