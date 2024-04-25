package com.swent.assos.ui.screens.calendar

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.data.CalendarDataSource
import com.swent.assos.model.data.Event
import com.swent.assos.model.view.CalendarViewModel
import java.time.LocalDate

@Preview(showSystemUi = true)
@Composable
fun Calendar(
    dayHeader: @Composable (day: LocalDate) -> Unit = { WeekDayHeader(day = it) },
    eventContent: @Composable (event: Event) -> Unit = { BasicEvent(event = it) },
) {
    val calendarViewModel: CalendarViewModel = hiltViewModel()
    val events = calendarViewModel._events.collectAsState()
  val dataSource = CalendarDataSource()
  var data by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
  val verticalScrollState = rememberScrollState()
  val horizontalScrollState = rememberScrollState()
  var sidebarWidth by remember { mutableIntStateOf(0) }
  val dayWidth = 256.dp
  val hourHeight = 64.dp

  Column(modifier = Modifier
      .padding(16.dp)
      .fillMaxSize()) {
    ChangeWeek(
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
        Modifier
            .padding(start = with(LocalDensity.current) { sidebarWidth.toDp() })
            .horizontalScroll(horizontalScrollState),
        dayHeader)

    Row {
      TimeSidebar(
          hourHeight = 64.dp,
          modifier =
          Modifier
              .verticalScroll(verticalScrollState)
              .onGloballyPositioned {
                  sidebarWidth = it.size.width
              })

      Event(
          events = events.value,
          eventContent = eventContent,
          dayWidth = dayWidth,
          hourHeight = hourHeight,
          data = data,
          modifier =
          Modifier
              .weight(1f)
              .verticalScroll(verticalScrollState)
              .horizontalScroll(horizontalScrollState))
    }
  }
}