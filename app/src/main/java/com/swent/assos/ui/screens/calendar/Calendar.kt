package com.swent.assos.ui.screens.calendar

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.data.CalendarDataSource
import com.swent.assos.model.data.Event
import com.swent.assos.model.view.CalendarViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Preview(showSystemUi = true)
@Composable
fun Calendar(
    dayHeader: @Composable (day: LocalDate) -> Unit = { WeekDayHeader(day = it) },
    eventContent: @Composable (event: Event) -> Unit = { BasicEvent(event = it) },
) {
  val calendarViewModel: CalendarViewModel = hiltViewModel()
  val events = calendarViewModel.events.collectAsState()
  val dataSource = CalendarDataSource()
  var data by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
  val verticalScrollState = rememberScrollState()
  val horizontalScrollState = rememberScrollState()
  var sidebarWidth by remember { mutableIntStateOf(0) }
  val dayWidth = 256.dp
  val hourHeight = 64.dp

  LazyColumn(modifier = Modifier
    .padding(16.dp)
    .fillMaxSize()) {
    item {
      Spacer(modifier = Modifier.height(16.dp))
      InfiniteScrollableDaysList()
    }

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
fun InfiniteScrollableDaysList() {
  val startDate = LocalDate.now() // Start from today's date

  // Generate a list of LocalDate objects representing days
  val daysList = remember {
    List(365 * 10) { // Generating 10 years worth of days
      startDate.plusDays(it.toLong())
    }
  }

  LazyRow {
    // Loop through the list of days infinitely
    items(daysList, key = { it.hashCode() }) { day ->
      DayItem(date = day)
      Spacer(modifier = Modifier.width(16.dp)) // Adjust spacing between items as needed
    }
  }
}

@Composable
fun DayItem(date: LocalDate) {
  Box(
    modifier = Modifier.wrapContentHeight()
  ) {
    Surface(
      color = Color.Transparent
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
      ) {
        Text(
          text = date.dayOfMonth.toString(),
          fontSize = 24.sp,
          fontWeight = FontWeight.SemiBold
        )
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
          }
        )
      }
    }
  }

  // Your UI for displaying each day item
}
