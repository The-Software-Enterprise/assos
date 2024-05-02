package com.swent.assos.ui.screens.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val HourFormatter = DateTimeFormatter.ofPattern("HH.mm")

@Composable
fun TimeSidebar(
    hourHeight: Dp,
    modifier: Modifier = Modifier,
    label: @Composable (time: LocalTime) -> Unit = { BasicSidebarLabel(time = it) },
) {
  Column(modifier = modifier) {
    val startTime = LocalTime.MIN
    repeat(24) { i ->
      Box(modifier = Modifier.height(hourHeight)) { label(startTime.plusHours(i.toLong())) }
    }
  }
}

@Composable
fun BasicSidebarLabel(time: LocalTime) {
  Text(text = time.format(HourFormatter), fontSize = 12.sp)
}
