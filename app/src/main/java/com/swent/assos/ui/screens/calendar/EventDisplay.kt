package com.swent.assos.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.swent.assos.model.data.Event
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.max
import kotlin.math.roundToInt

private const val NUMBER_OF_HOURS = 24

@Composable
fun BasicEvent(event: Event) {
  Column(
      modifier =
          Modifier.fillMaxSize()
              .padding(end = 2.dp, bottom = 2.dp)
              .background(Color(0xFFDE496E), shape = RoundedCornerShape(14.dp))
              .padding(4.dp)) {
        Text(
            text = event.title,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(4.dp))
      }
}

@Composable
fun Schedule(
    events: State<List<Event>>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (event: Event) -> Unit = { BasicEvent(event = it) },
    dayWidth: Dp,
    hourHeight: Dp
) {
  val dividerColor = Color.LightGray
  val offsetYHour = 25f
  Layout(
      content = {
        events.value.sortedBy(Event::startTime).forEach { event ->
          Box(modifier = Modifier.eventData(event)) { eventContent(event) }
        }
      },
      modifier =
          modifier.drawBehind {
            repeat(NUMBER_OF_HOURS - 1) {
              drawLine(
                  dividerColor,
                  start = Offset(0f, offsetYHour + (it + 1) * hourHeight.toPx()),
                  end = Offset(size.width, offsetYHour + (it + 1) * hourHeight.toPx()),
                  strokeWidth = 1.dp.toPx())
            }
          }) { measureables, constraints ->
        val height = hourHeight.roundToPx() * 24
        val width = dayWidth.roundToPx()
        val placeablesWithEvents =
            measureables.map { measurable ->
              val event = measurable.parentData as Event
              val eventDurationMinutes = ChronoUnit.MINUTES.between(event.startTime, event.endTime)
              val eventHeight =
                  max(0, ((eventDurationMinutes / 60f) * hourHeight.toPx()).roundToInt())
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
                ChronoUnit.MINUTES.between(LocalTime.MIN, event.startTime?.toLocalTime())
            val eventY = ((eventOffsetMinutes / 60f) * hourHeight.toPx()).roundToInt()

            placeable.place(0, offsetYHour.toInt() + eventY)
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
