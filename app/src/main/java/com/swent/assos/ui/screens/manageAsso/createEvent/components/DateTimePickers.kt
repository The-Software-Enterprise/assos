package com.swent.assos.ui.screens.manageAsso.createEvent.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.swent.assos.model.view.EventViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickers(viewModel: EventViewModel) {
  val context = LocalContext.current

  val event by viewModel.event.collectAsState()

  val startDatePickerState = rememberDatePickerState()
  var showStartDatePicker by remember { mutableStateOf(false) }
  val startTimePickerState = rememberTimePickerState()
  var showStartTimePicker by remember { mutableStateOf(false) }

  val endDatePickerState = rememberDatePickerState()
  var showEndDatePicker by remember { mutableStateOf(false) }
  val endTimePickerState = rememberTimePickerState()
  var showEndTimePicker by remember { mutableStateOf(false) }

  Row(
      modifier =
          Modifier.fillMaxWidth()
              .padding(10.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
              .height(35.dp),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier =
                Modifier.testTag("StartTimePicker").fillMaxSize().weight(1f).clickable {
                  showStartDatePicker = true
                },
            contentAlignment = Alignment.Center) {
              Text(
                  event.startTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                  fontSize = 13.sp,
                  color = MaterialTheme.colorScheme.onBackground)
            }
        Box(
            modifier =
                Modifier.width(1.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)))
        Box(
            modifier =
                Modifier.testTag("EndTimePicker").fillMaxSize().weight(1f).clickable {
                  showEndDatePicker = true
                },
            contentAlignment = Alignment.Center) {
              Text(
                  event.endTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                  fontSize = 13.sp,
                  color = MaterialTheme.colorScheme.onBackground)
            }
      }

  if (showStartDatePicker) {
    DatePickerDialog(
        modifier =
            Modifier.background(
                color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(30.dp)),
        onDismissRequest = {},
        confirmButton = {
          TextButton(
              onClick = {
                val selectedDate =
                    startDatePickerState.selectedDateMillis?.let {
                      Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                val currentDate = LocalDate.now()
                if (selectedDate != null && selectedDate.isBefore(currentDate)) {
                  Toast.makeText(
                          context,
                          "Selected date should be after today, please select again",
                          Toast.LENGTH_SHORT)
                      .show()
                } else {
                  showStartDatePicker = false
                  showStartTimePicker = true
                }
              }) {
                Text("OK")
              }
        },
        dismissButton = {
          TextButton(onClick = { showStartDatePicker = false }) { Text("Cancel") }
        }) {
          DatePicker(state = startDatePickerState)
        }
  }

  if (showStartTimePicker) {
    TimePickerDialog(
        onDismissRequest = {},
        confirmButton = {
          TextButton(
              modifier = Modifier.testTag("StartTimePickerConfirmButton"),
              onClick = {
                val current = LocalDateTime.now()
                val time = LocalTime.of(startTimePickerState.hour, startTimePickerState.minute)
                var selectedDate = current
                startDatePickerState.selectedDateMillis?.let {
                  selectedDate =
                      LocalDateTime.of(
                          Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate(),
                          time)
                }
                if (selectedDate.isBefore(current)) {
                  Toast.makeText(
                          context,
                          "Selected time should be after current time, please select again",
                          Toast.LENGTH_SHORT)
                      .show()
                } else {
                  event.startTime = selectedDate
                  showStartTimePicker = false
                }
              }) {
                Text("OK")
              }
        },
        dismissButton = {
          TextButton(onClick = { showStartTimePicker = false }) { Text("Cancel") }
        }) {
          TimePicker(state = startTimePickerState)
        }
  }

  if (showEndDatePicker) {
    DatePickerDialog(
        modifier =
            Modifier.background(
                color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(30.dp)),
        onDismissRequest = {},
        confirmButton = {
          TextButton(
              onClick = {
                val startDate =
                    startDatePickerState.selectedDateMillis?.let {
                      Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                val selectedEndDate =
                    endDatePickerState.selectedDateMillis?.let {
                      Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                if (selectedEndDate != null && selectedEndDate.isBefore(startDate)) {
                  Toast.makeText(
                          context,
                          "Selected end date should be after start date, please select again",
                          Toast.LENGTH_SHORT)
                      .show()
                } else {
                  showEndDatePicker = false
                  showEndTimePicker = true
                }
              }) {
                Text("OK")
              }
        },
        dismissButton = {
          TextButton(onClick = { showEndDatePicker = false }) { Text("Cancel") }
        }) {
          DatePicker(state = endDatePickerState)
        }
  }

  if (showEndTimePicker) {
    TimePickerDialog(
        onDismissRequest = {},
        confirmButton = {
          TextButton(
              modifier = Modifier.testTag("EndTimePickerConfirmButton"),
              onClick = {
                val startDate = event.startTime
                var endDate = startDate

                val time = LocalTime.of(endTimePickerState.hour, endTimePickerState.minute)
                endDatePickerState.selectedDateMillis?.let {
                  endDate =
                      LocalDateTime.of(
                          Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate(),
                          time)
                }
                if (endDate.isBefore(startDate)) {
                  Toast.makeText(
                          context,
                          "Selected end time should be after start time, please select again",
                          Toast.LENGTH_SHORT)
                      .show()
                } else {
                  event.endTime = endDate
                  showEndTimePicker = false
                }
              }) {
                Text("OK")
              }
        },
        dismissButton = {
          TextButton(
              modifier = Modifier.testTag("EndTimePickerCancelButton"),
              onClick = { showEndTimePicker = false }) {
                Text("Cancel")
              }
        }) {
          TimePicker(state = endTimePickerState)
        }
  }
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onDismissRequest: () -> Unit,
    confirmButton: @Composable (() -> Unit),
    dismissButton: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable () -> Unit,
) {
  Dialog(
      onDismissRequest = onDismissRequest,
      properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        tonalElevation = 6.dp,
        modifier =
            Modifier.width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(shape = MaterialTheme.shapes.extraLarge, color = containerColor),
        color = containerColor) {
          Column(
              modifier = Modifier.padding(24.dp).testTag("TimePickerDialog"),
              horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium)
                content()
                Row(modifier = Modifier.height(40.dp).fillMaxWidth()) {
                  Spacer(modifier = Modifier.weight(1f))
                  dismissButton?.invoke()
                  confirmButton()
                }
              }
        }
  }
}
