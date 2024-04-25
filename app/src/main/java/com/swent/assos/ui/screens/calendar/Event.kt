package com.swent.assos.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.swent.assos.model.data.CalendarUiModel
import com.swent.assos.model.data.Event
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

private val EventTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")

@Composable
fun BasicEvent(
    event: Event,
    modifier: Modifier = Modifier,
) {
  Column(
      modifier =
          modifier
              .fillMaxSize()
              .padding(end = 2.dp, bottom = 2.dp)
              .background(Color.Blue, shape = RoundedCornerShape(4.dp))
              .padding(4.dp)) {
        Text(
            text =
                "${event.startTime.format(EventTimeFormatter)} - ${event.endTime.format(
                EventTimeFormatter
            )}",
        )

        Text(
            text = event.title,
            fontWeight = FontWeight.Bold,
        )

        Text(
          text = event.description,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          )
      }
}

@Composable
fun Event(
    events: List<Event>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (event: Event) -> Unit = { BasicEvent(event = it) },
    dayWidth: Dp,
    hourHeight: Dp,
    data: CalendarUiModel,
) {
  val numDays = 7
  val dividerColor = Color.LightGray
  Layout(
      content = {
        events.sortedBy(Event::startTime).forEach { event ->
          Box(modifier = Modifier.eventData(event)) { eventContent(event) }
        }
      },
      modifier =
          modifier.drawBehind {
            repeat(23) {
              drawLine(
                  dividerColor,
                  start = Offset(0f, (it + 1) * hourHeight.toPx()),
                  end = Offset(size.width, (it + 1) * hourHeight.toPx()),
                  strokeWidth = 1.dp.toPx())
            }
            repeat(numDays - 1) {
              drawLine(
                  dividerColor,
                  start = Offset((it + 1) * dayWidth.toPx(), 0f),
                  end = Offset((it + 1) * dayWidth.toPx(), size.height),
                  strokeWidth = 1.dp.toPx())
            }
          }) { measureables, constraints ->
        val height = hourHeight.roundToPx() * 24
        val width = dayWidth.roundToPx() * numDays
        val placeablesWithEvents =
            measureables.map { measurable ->
              val event = measurable.parentData as Event
              val eventDurationMinutes = ChronoUnit.MINUTES.between(event.startTime, event.endTime)
              val eventHeight = ((eventDurationMinutes / 60f) * hourHeight.toPx()).roundToInt()
              val placeable =
                  measurable.measure(
                      constraints.copy(
                          minWidth = dayWidth.roundToPx(),
                          maxWidth = dayWidth.roundToPx(),
                          minHeight = eventHeight,
                          maxHeight = eventHeight))
              Pair(placeable, event)
            }
        layout(width, height) {
          placeablesWithEvents.forEach { (placeable, event) ->
            val eventOffsetMinutes =
                ChronoUnit.MINUTES.between(LocalTime.MIN, event.startTime.toLocalTime())
            val eventY = ((eventOffsetMinutes / 60f) * hourHeight.toPx()).roundToInt()

            val eventOffsetDays =
                ChronoUnit.DAYS.between(data.startDate.date, event.startTime.toLocalDate()).toInt()
            val eventX = eventOffsetDays * dayWidth.roundToPx()

            placeable.place(eventX, eventY)
          }
        }
      }
}

private fun Modifier.eventData(event: Event) = this.then(CalendarEventDataModifier(event))

private class CalendarEventDataModifier(
    val event: Event,
) : ParentDataModifier {
  override fun Density.modifyParentData(parentData: Any?) = event
}
