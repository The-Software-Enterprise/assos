package com.swent.assos.ui.screens.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swent.assos.R
import com.swent.assos.model.view.CalendarViewModel
import java.time.format.DateTimeFormatter

@Composable
fun Reminder(calendarViewModel: CalendarViewModel) {

  val tomorrowEvents by calendarViewModel.tomorrowEvents.collectAsState()

  LaunchedEffect(Unit) { calendarViewModel.updateEvents() }

  val listState = rememberLazyListState()

  LaunchedEffect(listState) {
    snapshotFlow { listState.layoutInfo.visibleItemsInfo }
        .collect { visibleItems ->
          if (tomorrowEvents.isNotEmpty() &&
              visibleItems.isNotEmpty() &&
              visibleItems.last().index == tomorrowEvents.size - 1) {
            calendarViewModel.loadMoreEvents()
          }
        }
  }

  Column(modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("ReminderScreen")) {
    Text(
        modifier = Modifier.testTag("Reminder"),
        text = "Reminder",
        style =
            TextStyle(
                fontSize = 16.sp,
                lineHeight = 26.sp,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                fontWeight = FontWeight(600),
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = 0.3.sp,
            ))
    Spacer(modifier = Modifier.height(14.dp))
    Text(
        modifier = Modifier.testTag("Description"),
        text = "Don't forget schedule for tomorrow",
        style =
            TextStyle(
                fontSize = 12.sp,
                lineHeight = 26.sp,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                fontWeight = FontWeight(400),
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = 0.5.sp,
            ))
    Spacer(modifier = Modifier.height(14.dp))
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        userScrollEnabled = true,
        state = listState,
        modifier = Modifier.testTag("ReminderList")) {
          for (event in tomorrowEvents) {
            item {
              Box(
                  modifier =
                      Modifier.fillMaxWidth()
                          .height(64.dp)
                          .background(
                              color = Color(0xFF8572FF), shape = RoundedCornerShape(size = 10.dp))
                          .testTag("Item")) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                      Box(
                          modifier =
                              Modifier.padding(start = 10.dp, top = 8.dp)
                                  .width(48.dp)
                                  .height(48.dp)
                                  .background(
                                      color = Color(0xFFBAB0F9),
                                      shape = RoundedCornerShape(size = 10.dp)),
                          contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(R.drawable.calendar),
                                contentDescription = null)
                          }
                      Column {
                        Text(
                            modifier = Modifier.padding(start = 27.dp, top = 8.dp).fillMaxWidth(),
                            text = "${event.second.title} - ${event.first}",
                            style =
                                TextStyle(
                                    fontSize = 12.sp,
                                    lineHeight = 26.sp,
                                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFFFFFFFF),
                                    letterSpacing = 0.5.sp,
                                ))
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(
                            modifier = Modifier.padding(start = 27.dp, top = 8.dp).fillMaxWidth(),
                        ) {
                          Image(painterResource(R.drawable.time_circle), contentDescription = null)
                          Spacer(modifier = Modifier.width(6.dp))
                          Text(
                              modifier = Modifier.padding(top = 2.dp),
                              text =
                                  "${event.second.startTime?.format(DateTimeFormatter.ofPattern("HH"))}" +
                                      "." +
                                      "${event.second.startTime?.format(DateTimeFormatter.ofPattern("mm"))}" +
                                      " - " +
                                      "${event.second.endTime?.format(DateTimeFormatter.ofPattern("HH"))}" +
                                      "." +
                                      "${event.second.endTime?.format(DateTimeFormatter.ofPattern("mm"))}",
                              style =
                                  TextStyle(
                                      fontSize = 10.sp,
                                      lineHeight = 26.sp,
                                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                                      fontWeight = FontWeight(400),
                                      color = Color(0xFFFFFFFF),
                                      letterSpacing = 0.5.sp,
                                  ))
                        }
                      }
                    }
                  }
              Spacer(Modifier.height(14.dp))
            }
          }
        }
  }
}
