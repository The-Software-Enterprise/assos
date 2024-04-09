package com.swent.assos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.swent.assos.ui.theme.AssosTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
fun WeeklyCalendar() {
    // Use AssosTheme and display events from the sampleEvents list
    AssosTheme {
        // Assuming you want to display all events in the list
        Schedule(sampleEvents)
    }
}

val EventTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")
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
            text = "${event.startTime.format(EventTimeFormatter)} - ${event.endTime.format(EventTimeFormatter)}",
            //style = MaterialTheme.typography.caption,
        )

        Text(
            text = event.name,
            //style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold,
        )

        if (event.description != null) {
            Text(
                text = event.description,
                //style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private val sampleEvents = listOf(
    CalendarEvent(
        name = "Google I/O Keynote",
        color = Color(0xFFAFBBF2),
        startTime = LocalDateTime.parse("2021-05-18T09:00:00"),
        endTime = LocalDateTime.parse("2021-05-18T11:00:00"),
        description = "Tune in to find out about how we're furthering our mission to organize the world’s information and make it universally accessible and useful.",
    ),
    CalendarEvent(
        name = "Developer Keynote",
        color = Color(0xFFAFBBF2),
        startTime = LocalDateTime.parse("2021-05-18T11:15:00"),
        endTime = LocalDateTime.parse("2021-05-18T12:15:00"),
        description = "Learn about the latest updates to our developer products and platforms from Google Developers.",
    ),
    CalendarEvent(
        name = "What's new in Android",
        color = Color(0xFF1B998B),
        startTime = LocalDateTime.parse("2021-05-18T12:30:00"),
        endTime = LocalDateTime.parse("2021-05-18T15:00:00"),
        description = "In this Keynote, Chet Haase, Dan Sandler, and Romain Guy discuss the latest Android features and enhancements for developers.",
    ),
    CalendarEvent(
        name = "What's new in Machine Learning",
        color = Color(0xFFF4BFDB),
        startTime = LocalDateTime.parse("2021-05-19T09:30:00"),
        endTime = LocalDateTime.parse("2021-05-19T11:00:00"),
        description = "Learn about the latest and greatest in ML from Google. We’ll cover what’s available to developers when it comes to creating, understanding, and deploying models for a variety of different applications.",
    ),
    CalendarEvent(
        name = "What's new in Material Design",
        color = Color(0xFF6DD3CE),
        startTime = LocalDateTime.parse("2021-05-19T11:00:00"),
        endTime = LocalDateTime.parse("2021-05-19T12:15:00"),
        description = "Learn about the latest design improvements to help you build personal dynamic experiences with Material Design.",
    ),
    CalendarEvent(
        name = "Jetpack Compose Basics",
        color = Color(0xFF1B998B),
        startTime = LocalDateTime.parse("2021-05-20T12:00:00"),
        endTime = LocalDateTime.parse("2021-05-20T13:00:00"),
        description = "This Workshop will take you through the basics of building your first app with Jetpack Compose, Android's new modern UI toolkit that simplifies and accelerates UI development on Android.",
    ),
)

class EventsProvider : PreviewParameterProvider<CalendarEvent> {
    override val values = sampleEvents.asSequence()
}

@Composable
fun Schedule(
    events: List<CalendarEvent>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (event: CalendarEvent) -> Unit = { BasicEvent(event = it) },
    dayHeader: @Composable (day: LocalDate) -> Unit = { BasicDayHeader(day = it) },
    minDate: LocalDate = events.minByOrNull(CalendarEvent::startTime)!!.startTime.toLocalDate(),
    maxDate: LocalDate = events.maxByOrNull(CalendarEvent::endTime)!!.endTime.toLocalDate(),
) {
    val dayWidth = 256.dp
    val hourHeight = 64.dp
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    var sidebarWidth by remember { mutableStateOf(0) }
    Column(modifier = modifier) {
        ScheduleHeader(
            minDate = minDate,
            maxDate = maxDate,
            dayWidth = dayWidth,
            dayHeader = dayHeader,
            modifier = Modifier
                .padding(start = with(LocalDensity.current) { sidebarWidth.toDp() })
                .horizontalScroll(horizontalScrollState)
        )
        Row(modifier = Modifier.weight(1f)) {
            ScheduleSidebar(
                hourHeight = hourHeight,
                modifier = Modifier
                    .verticalScroll(verticalScrollState)
                    .onGloballyPositioned { sidebarWidth = it.size.width }
            )
            BasicSchedule(
                events = events,
                eventContent = eventContent,
                minDate = minDate,
                maxDate = maxDate,
                dayWidth = dayWidth,
                hourHeight = hourHeight,
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(verticalScrollState)
                    .horizontalScroll(horizontalScrollState)
            )
        }
    }
}

@Composable
fun BasicSchedule(
    events: List<CalendarEvent>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (event: CalendarEvent) -> Unit = { BasicEvent(event = it) },
    minDate: LocalDate = events.minByOrNull(CalendarEvent::startTime)!!.startTime.toLocalDate(),
    maxDate: LocalDate = events.maxByOrNull(CalendarEvent::endTime)!!.endTime.toLocalDate(),
    dayWidth: Dp,
    hourHeight: Dp,
) {
    val numDays = ChronoUnit.DAYS.between(minDate, maxDate).toInt() + 1
    val dividerColor = Color.LightGray
    Layout(
        content = {
            events.sortedBy(CalendarEvent::startTime).forEach { event ->
                Box(modifier = Modifier.eventData(event)) {
                    eventContent(event)
                }
            }
        },
        modifier = modifier.drawBehind {
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
        },
    ) { measureables, constraints ->
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
                val eventOffsetDays = ChronoUnit.DAYS.between(minDate, event.startTime.toLocalDate()).toInt()
                val eventX = eventOffsetDays * dayWidth.roundToPx()
                placeable.place(eventX, eventY)
            }
        }
    }

}

private class EventDataModifier(
    val event: CalendarEvent,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = event
}
private fun Modifier.eventData(event: CalendarEvent) = this.then(EventDataModifier(event))

private val DayFormatter = DateTimeFormatter.ofPattern("EE, MMM d")

@Composable
fun BasicDayHeader(
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

@Preview(showBackground = true)
@Composable
fun BasicDayHeaderPreview() {
    AssosTheme {
        BasicDayHeader(day = LocalDate.now())
    }
}

@Composable
fun ScheduleHeader(
    minDate: LocalDate,
    maxDate: LocalDate,
    dayWidth: Dp,
    modifier: Modifier = Modifier,
    dayHeader: @Composable (day: LocalDate) -> Unit = { BasicDayHeader(day = it) },
) {
    Row(modifier = modifier) {
        val numDays = ChronoUnit.DAYS.between(minDate, maxDate).toInt() + 1
        repeat(numDays) { i ->
            Box(modifier = Modifier.width(dayWidth)) {
                dayHeader(minDate.plusDays(i.toLong()))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleHeaderPreview() {
    AssosTheme {
        ScheduleHeader(
            minDate = LocalDate.now(),
            maxDate = LocalDate.now().plusDays(5),
            dayWidth = 256.dp,
        )
    }
}

private val HourFormatter = DateTimeFormatter.ofPattern("h a")

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

@Preview(showBackground = true)
@Composable
fun BasicSidebarLabelPreview() {
    AssosTheme {
        BasicSidebarLabel(time = LocalTime.NOON, Modifier.sizeIn(maxHeight = 64.dp))
    }
}

@Composable
fun ScheduleSidebar(
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

@Preview(showBackground = true)
@Composable
fun ScheduleSidebarPreview() {
    AssosTheme {
        ScheduleSidebar(hourHeight = 64.dp)
    }
}