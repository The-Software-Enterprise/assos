package com.swent.assos.ui.screens.calendar

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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
