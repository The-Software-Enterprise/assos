package com.swent.assos.ui.screens.calendar

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swent.assos.model.data.CalendarDataSource
import com.swent.assos.model.data.CalendarEvent
import com.swent.assos.ui.components.TimeSidebar
import com.swent.assos.ui.screens.overview.BasicEvent
import com.swent.assos.ui.screens.overview.Event
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showSystemUi = true)
@Composable
fun Calendar(
    dayHeader: @Composable (day: LocalDate) -> Unit = { WeekDayHeader(day = it) },
    eventContent: @Composable (event: CalendarEvent) -> Unit = { BasicEvent(event = it) },
) {

  val dataSource = CalendarDataSource()
  var data by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
  val verticalScrollState = rememberScrollState()
  val horizontalScrollState = rememberScrollState()
  var sidebarWidth by remember { mutableIntStateOf(0) }
  val dayWidth = 256.dp
  val hourHeight = 64.dp

  Column(
      modifier =
          Modifier.padding(16.dp)
              .fillMaxSize()
              .semantics { testTagsAsResourceId = true }
              .testTag("CalendarScreen")) {
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
              events = sampleEvents,
              eventContent = eventContent,
              dayWidth = dayWidth,
              hourHeight = hourHeight,
              data = data,
              modifier =
                  Modifier.weight(1f)
                      .verticalScroll(verticalScrollState)
                      .horizontalScroll(horizontalScrollState))
        }
      }
}

private val sampleEvents =
    listOf(
        CalendarEvent(
            name = "Sprint 3 Meeting",
            color = Color(0xFFAFBBF2),
            startTime = LocalDateTime.parse("2024-04-12T11:00:00"),
            endTime = LocalDateTime.parse("2024-04-12T13:00:00"),
            description = "Discuss the progress of the current sprint and plan the next sprint.",
        ),
        CalendarEvent(
            name = "Sprint 4 Meeting",
            color = Color(0xFFAFBBF2),
            startTime = LocalDateTime.parse("2024-04-19T11:00:00"),
            endTime = LocalDateTime.parse("2024-04-19T13:00:00"),
            description = "Discuss the progress of the current sprint and plan the next sprint.",
        ),
        CalendarEvent(
            name = "Swent App Progress Meeting",
            color = Color(0xFF1B998B),
            startTime = LocalDateTime.parse("2024-04-11T09:00:00"),
            endTime = LocalDateTime.parse("2024-04-11T12:00:00"),
            description = "Discuss the progress of the Swent app and plan the next steps.",
        ),
    )
