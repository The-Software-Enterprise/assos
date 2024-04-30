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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.data.CalendarDataSource
import com.swent.assos.model.data.CalendarUiModel
import com.swent.assos.model.data.Event
import com.swent.assos.model.view.CalendarViewModel
import java.time.DayOfWeek
import java.time.LocalDate

private val dayWidth = 256.dp
private val hourHeight = 64.dp

@Preview(showSystemUi = true)
@Composable
fun Calendar(
    eventContent: @Composable (event: Event) -> Unit = { BasicEvent(event = it) },
) {
  val calendarViewModel: CalendarViewModel = hiltViewModel()
  val events = calendarViewModel.events.collectAsState()
  val dataSource = CalendarDataSource()
  val data by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
  val verticalScrollState = rememberScrollState(1350)
  var selectedDate by remember { mutableStateOf(LocalDate.now()) }

  Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
    Spacer(modifier = Modifier.height(16.dp))
    InfiniteScrollableDaysList(selectedDate = selectedDate, onDateSelected = { selectedDate = it })

    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text =
            if (selectedDate == LocalDate.now()) {
              "Schedule Today"
            } else {
              "Daily Schedule"
            },
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF1E293B))

    Spacer(modifier = Modifier.height(32.dp))
    DailySchedule(selectedDate, verticalScrollState, data, eventContent)

    /*ChangeWeek(
        data = data,
        onPrevClickListener = { startDate ->
          val finalStartDate = startDate.minusDays(1)
          data =
              dataSource.getData(
                  startDate = finalStartDate, lastSelectedDate = data.selectedDate.date)
        },
        onNextClickListener = { endDate ->
          val finalStartDate = endDate.plusDays(2)
          data =
              dataSource.getData(
                  startDate = finalStartDate, lastSelectedDate = data.selectedDate.date)
        })

    WeekHeader(
        data = data,
        dayWidth = dayWidth,
        modifier =
            Modifier.padding(start = with(LocalDensity.current) { sidebarWidth.toDp() })
                .horizontalScroll(horizontalScrollState),
        dayHeader)

    Row {
      TimeSidebar(
          hourHeight = 64.dp,
          modifier =
              Modifier.verticalScroll(verticalScrollState).onGloballyPositioned {
                sidebarWidth = it.size.width
              })

      Event(
          events = events.value,
          eventContent = eventContent,
          dayWidth = dayWidth,
          hourHeight = hourHeight,
          data = data,
          modifier =
              Modifier.weight(1f)
                  .verticalScroll(verticalScrollState)
                  .horizontalScroll(horizontalScrollState))
    }*/
  }
}

@Composable
fun InfiniteScrollableDaysList(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
  val startDate = LocalDate.now().minusDays(3L) // Start from 3 days ago

  // Generate a list of LocalDate objects representing days
  val daysList = remember {
    List(365 * 10) { // Generating 10 years worth of days
      startDate.plusDays(it.toLong())
    }
  }

  LazyRow {
    // Loop through the list of days infinitely
    itemsIndexed(daysList) { index, day ->
      DayItem(date = day, selected = day == selectedDate, onDateSelected)
      if (day == selectedDate || index < daysList.size - 1 && daysList[index + 1] == selectedDate) {
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
            Modifier.width(53.dp) // 53
                .height(79.dp)
          } else {
            Modifier.width(32.dp) // 53
                .height(79.dp)
                .clickable(onClick = { onDateSelected(date) })
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

  // Your UI for displaying each day item
}

@Composable
fun DailySchedule(
    date: LocalDate,
    verticalScrollState: ScrollState,
    data: CalendarUiModel,
    eventContent: @Composable (event: Event) -> Unit
) {
  /*TODO : get events for the selected date*/
  val events = emptyList<Event>()
  Row(modifier = Modifier.height(208.dp)) {
    TimeSidebar(hourHeight = hourHeight, modifier = Modifier.verticalScroll(verticalScrollState))

    Schedule(
        events = events,
        eventContent = eventContent,
        dayWidth = dayWidth,
        hourHeight = hourHeight,
        data = data,
        modifier = Modifier.weight(1f).verticalScroll(verticalScrollState))
  }
}
