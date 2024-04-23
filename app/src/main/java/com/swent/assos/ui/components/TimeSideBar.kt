package com.swent.assos.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val HourFormatter = DateTimeFormatter.ofPattern("h a")

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
fun BasicSidebarLabel(
    time: LocalTime,
    modifier: Modifier = Modifier,
) {
  Text(text = time.format(HourFormatter), modifier = modifier.fillMaxHeight().padding(4.dp))
}
