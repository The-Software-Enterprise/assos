package com.swent.assos.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.swent.assos.ui.theme.AssosTheme
import androidx.compose.ui.Modifier
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@Preview(showSystemUi = true)
@Composable
fun CalendarAppPreview() {
  AssosTheme {
    Calendar(
      events = sampleEvents,
      modifier = Modifier.padding(16.dp)
    )
  }
}

private val DayFormatter = DateTimeFormatter.ofPattern("EE, MMM d")
private val HourFormatter = DateTimeFormatter.ofPattern("h a")
private val EventTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")

@Composable
fun Calendar(
  events: List<CalendarEvent>,
  modifier: Modifier = Modifier,
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

  Column(modifier = modifier.fillMaxSize()) {

    ChangeWeek(
      data = data,
      onPrevClickListener = { startDate ->
        val finalStartDate = startDate.minusDays(1)
        data = dataSource.getData(startDate = finalStartDate, lastSelectedDate = data.selectedDate.date)
      },
      onNextClickListener = { endDate ->
        val finalStartDate = endDate.plusDays(2)
        data = dataSource.getData(startDate = finalStartDate, lastSelectedDate = data.selectedDate.date)
      }
    )

    WeekHeader(
      data = data, dayWidth = dayWidth, modifier = Modifier
      .padding(start = with(LocalDensity.current) { sidebarWidth.toDp() })
      .horizontalScroll(horizontalScrollState), dayHeader
    )

    Row {

      TimeSidebar(
        hourHeight = 64.dp,
        modifier = Modifier
          .verticalScroll(verticalScrollState)
          .onGloballyPositioned { sidebarWidth = it.size.width }
      )

      Event(
        events = events,
        eventContent = eventContent,
        dayWidth = dayWidth,
        hourHeight = hourHeight,
        data = data,
        modifier = Modifier
          .weight(1f)
          .verticalScroll(verticalScrollState)
          .horizontalScroll(horizontalScrollState)
      )
    }
  }
}

@Composable
fun ChangeWeek(
  data: CalendarUiModel,
  onPrevClickListener: (LocalDate) -> Unit,
  onNextClickListener: (LocalDate) -> Unit,
) {
  Row {
    IconButton(onClick = {
      onPrevClickListener(data.startDate.date)
    }) {
      Icon(
        imageVector = Icons.Filled.ChevronLeft,
        contentDescription = "Back"
      )
    }
    IconButton(onClick = {
      onNextClickListener(data.endDate.date)
    }) {
      Icon(
        imageVector = Icons.Filled.ChevronRight,
        contentDescription = "Next"
      )
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
      Box(modifier = Modifier.width(dayWidth)
      ) {
        dayHeader(date.date)
      }
    }
  }
}

@Composable
fun TimeSidebar(
  hourHeight: Dp,
  modifier: Modifier = Modifier,
  label: @Composable (time: LocalTime) -> Unit = { BasicSidebarLabel(time = it) },
) {
  Column(modifier = modifier) {
    val startTime = LocalTime.MIN
    repeat(24) { i ->
      Box(modifier = Modifier.height(hourHeight)) {
        label(startTime.plusHours(i.toLong()))
      }
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
    modifier = modifier
      .fillMaxWidth()
      .padding(4.dp)
  )
}

@Composable
fun Event(
  events: List<CalendarEvent>,
  modifier: Modifier = Modifier,
  eventContent: @Composable (event: CalendarEvent) -> Unit = { BasicEvent(event = it) },
  dayWidth: Dp,
  hourHeight: Dp,
  data: CalendarUiModel,
) {
  val numDays = 7
  val dividerColor = Color.LightGray
  Layout(
    content = {
      events.sortedBy(CalendarEvent::startTime).forEach { event ->

          Box(modifier = Modifier.eventData(event)) {
            eventContent(event)
          }

      }
    },
    modifier = modifier
      .drawBehind {
        repeat(23) {
          drawLine(
            dividerColor,
            start = Offset(0f, (it + 1) * hourHeight.toPx()),
            end = Offset(size.width, (it + 1) * hourHeight.toPx()),
            strokeWidth = 1.dp.toPx()
          )
        }
        repeat(numDays - 1) {
          drawLine(
            dividerColor,
            start = Offset((it + 1) * dayWidth.toPx(), 0f),
            end = Offset((it + 1) * dayWidth.toPx(), size.height),
            strokeWidth = 1.dp.toPx()
          )
        }
      }
  ){ measureables, constraints ->
    val height = hourHeight.roundToPx() * 24
    val width = dayWidth.roundToPx() * numDays
    val placeablesWithEvents = measureables.map { measurable ->
      val event = measurable.parentData as CalendarEvent
      val eventDurationMinutes = ChronoUnit.MINUTES.between(event.startTime, event.endTime)
      val eventHeight = ((eventDurationMinutes / 60f) * hourHeight.toPx()).roundToInt()
      val placeable = measurable.measure(constraints.copy(minWidth = dayWidth.roundToPx(), maxWidth = dayWidth.roundToPx(), minHeight = eventHeight, maxHeight = eventHeight))
      Pair(placeable, event)
    }
    layout(width, height) {

      placeablesWithEvents.forEach { (placeable, event) ->
        val eventOffsetMinutes = ChronoUnit.MINUTES.between(LocalTime.MIN, event.startTime.toLocalTime())
        val eventY = ((eventOffsetMinutes / 60f) * hourHeight.toPx()).roundToInt()


        val eventOffsetDays = ChronoUnit.DAYS.between(data.startDate.date, event.startTime.toLocalDate()).toInt()
        val eventX = eventOffsetDays * dayWidth.roundToPx()

        placeable.place(eventX, eventY)
      }
    }
  }
}

@Composable
fun BasicEvent(
  event: CalendarEvent,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(end = 2.dp, bottom = 2.dp)
      .background(event.color, shape = RoundedCornerShape(4.dp))
      .padding(4.dp)
  ) {
    Text(
      text = "${event.startTime.format(EventTimeFormatter)} - ${event.endTime.format(
        EventTimeFormatter
      )}",
    )

    Text(
      text = event.name,
      fontWeight = FontWeight.Bold,
    )

    if (event.description != null) {
      Text(
        text = event.description,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}

@Composable
fun BasicSidebarLabel(
  time: LocalTime,
  modifier: Modifier = Modifier,
) {
  Text(
    text = time.format(HourFormatter),
    modifier = modifier
      .fillMaxHeight()
      .padding(4.dp)
  )
}

private class CalendarEventDataModifier(
  val event: CalendarEvent,
) : ParentDataModifier {
  override fun Density.modifyParentData(parentData: Any?) = event
}

private fun Modifier.eventData(event: CalendarEvent) = this.then(CalendarEventDataModifier(event))

private val sampleEvents = listOf(
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
