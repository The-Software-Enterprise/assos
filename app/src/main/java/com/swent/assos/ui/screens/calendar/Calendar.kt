package com.swent.assos.ui.screens.calendar

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.data.Event
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.CalendarViewModel
import com.swent.assos.ui.components.LoadingCircle
import com.swent.assos.ui.components.PageTitle
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dayWidth = 256.dp
private val hourHeight = 50.dp
private val dateFormatter = DateTimeFormatter.ofPattern("dd LLL uuuu")

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Calendar(
    navigationActions: NavigationActions,
    eventContent: @Composable (event: Event) -> Unit = {
      BasicEvent(event = it, navigationActions = navigationActions)
    },
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
          Column(modifier = Modifier.padding(it).fillMaxSize()) {
            InfiniteScrollableDaysList(
                selectedDate = selectedDate,
                onDateSelected = { newDate -> calendarViewModel.updateSelectedDate(newDate) })
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                  Spacer(modifier = Modifier.height(10.dp))
                  Text(
                      text =
                          if (selectedDate.value == LocalDate.now()) {
                            "Schedule Today"
                          } else {
                            "Daily Schedule"
                          },
                      fontSize = 16.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = MaterialTheme.colorScheme.onBackground,
                      modifier = Modifier.align(Alignment.Start))
                  Spacer(modifier = Modifier.height(10.dp))
                  Box(modifier = Modifier.fillMaxHeight().weight(0.55f)) {
                    DailySchedule(
                        events = selectedEvents,
                        verticalScrollState = verticalScrollState,
                        eventContent = eventContent)
                  }
                  Spacer(modifier = Modifier.height(10.dp))
                  Box(modifier = Modifier.fillMaxHeight().weight(0.45f)) {
                    Reminder(
                        calendarViewModel = calendarViewModel,
                        navigationActions = navigationActions)
                  }
                }
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
    items(daysList) { day ->
      DayItem(date = day, selected = day == selectedDate.value, onDateSelected)
    }
  }
}

@Composable
fun DayItem(date: LocalDate, selected: Boolean, onDateSelected: (LocalDate) -> Unit) {
  Column(
      modifier =
          if (selected) {
            Modifier.width(53.dp)
                .height(70.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFFFF0F0))
                .testTag("DayItemSelected")
          } else {
            Modifier.width(53.dp)
                .height(70.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = { onDateSelected(date) })
                .testTag("DayItem")
          },
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
  }
}

@Composable
fun DailySchedule(
    events: State<List<Event>>,
    verticalScrollState: ScrollState,
    eventContent: @Composable (event: Event) -> Unit
) {
  Row(modifier = Modifier.testTag("EventUI").fillMaxSize()) {
    TimeSidebar(hourHeight = hourHeight, modifier = Modifier.verticalScroll(verticalScrollState))
    Schedule(
        events = events,
        eventContent = eventContent,
        dayWidth = dayWidth,
        hourHeight = hourHeight,
        modifier = Modifier.weight(1f).verticalScroll(verticalScrollState))
  }
}
