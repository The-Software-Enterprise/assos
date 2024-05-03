package com.swent.assos.ui.screens.manageAsso

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.maxkeppeker.sheets.core.models.base.ButtonStyle
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.date_time.DateTimeDialog
import com.maxkeppeler.sheets.date_time.models.DateTimeSelection
import com.swent.assos.R
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.model.view.HourFormat
import com.swent.assos.ui.components.PageTitleWithGoBack
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateEvent(assoId: String, navigationActions: NavigationActions) {
  val viewModel: EventViewModel = hiltViewModel()

  val event by viewModel.event.collectAsState()
  val hourFormat by viewModel.hourFormat.collectAsState()

  var showTimePickerStart by remember { mutableStateOf(false) }
  var showTimePickerEnd by remember { mutableStateOf(false) }

  var startDatePickerState = rememberDatePickerState()
  var showStartDatePicker by remember { mutableStateOf(false) }
  var startTimePickerState = rememberTimePickerState()
  var showStartTimePicker by remember { mutableStateOf(false) }

  var endDatePickerState = rememberDatePickerState()
  var showEndDatePicker by remember { mutableStateOf(false) }
  var endTimePickerState = rememberTimePickerState()
  var showEndTimePicker by remember { mutableStateOf(false) }

  val launcher =
      rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result
        ->
        if (result.resultCode == Activity.RESULT_OK) {
          viewModel.setImage(result.data?.data)
        }
      }

  LaunchedEffect(key1 = Unit) { event.associationId = assoId }

  val context = LocalContext.current

  if (showStartDatePicker) {
    DatePickerDialog(
        onDismissRequest = {},
        confirmButton = {
          TextButton(
              modifier = Modifier.testTag("StartDatePickerConfirmButton"),
              onClick = {
                val selectedDate =
                    Instant.ofEpochMilli(startDatePickerState.selectedDateMillis!!)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                val currentDate = LocalDate.now()
                if (selectedDate.isBefore(currentDate)) {
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
        onDismissRequest = {},
        confirmButton = {
          TextButton(
              onClick = {
                val startDate =
                    Instant.ofEpochMilli(startDatePickerState.selectedDateMillis!!)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                val selectedEndDate =
                    Instant.ofEpochMilli(endDatePickerState.selectedDateMillis!!)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

                if (selectedEndDate.isBefore(startDate)) {
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
                if (endDate == null || endDate!!.isBefore(startDate)) {
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

  if (showTimePickerStart) {
    DateTimeDialog(
        state =
            rememberUseCaseState(visible = true, onCloseRequest = { showTimePickerStart = false }),
        selection =
            DateTimeSelection.DateTime(
                selectedDate = event.startTime?.toLocalDate(),
                extraButton = SelectionButton(hourFormat.name, type = ButtonStyle.FILLED),
                onExtraButtonClick = { viewModel.switchHourFormat() }) {
                  if (event.endTime == null) {
                    event.startTime = convertTo24from(it, hourFormat)
                  } else if (it.isBefore(event.endTime)) {
                    event.startTime = convertTo24from(it, hourFormat)
                  }
                })
  }

  if (showTimePickerEnd) {
    DateTimeDialog(
        state =
            rememberUseCaseState(visible = true, onCloseRequest = { showTimePickerEnd = false }),
        selection =
            DateTimeSelection.DateTime(
                selectedDate = event.endTime?.toLocalDate(),
                extraButton = SelectionButton(hourFormat.name),
                onExtraButtonClick = { viewModel.switchHourFormat() }) {
                  if (it.isAfter(LocalDateTime.now())) {
                    if (event.startTime == null) {
                      event.endTime = convertTo24from(it, hourFormat)
                    } else if (it.isAfter(event.startTime)) {
                      event.endTime = convertTo24from(it, hourFormat)
                    }
                  }
                })
  }

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("CreateEventScreen"),
      topBar = {
        PageTitleWithGoBack(title = "Create an event", navigationActions = navigationActions)
      }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).fillMaxWidth().testTag("Form"),
            horizontalAlignment = Alignment.CenterHorizontally) {
              item {
                OutlinedTextField(
                    modifier =
                        Modifier.fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 8.dp)
                            .testTag("InputTitle"),
                    value = event.title,
                    onValueChange = { viewModel.setTitle(it) },
                    textStyle =
                        TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.sf_pro_display_regular))),
                    label = { Text(text = "Title") },
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.secondary,
                            focusedLabelColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.secondary))

                OutlinedTextField(
                    modifier =
                        Modifier.fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 8.dp)
                            .height(150.dp)
                            .testTag("InputDescription"),
                    value = event.description,
                    onValueChange = { viewModel.setDescription(it) },
                    singleLine = false,
                    textStyle =
                        TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.sf_pro_display_regular))),
                    label = { Text(text = "Description") },
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.secondary,
                            focusedLabelColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.secondary))

                Box(
                    modifier =
                        Modifier.padding(8.dp)
                            .width(120.dp)
                            .height(150.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .testTag("InputImage")
                            .clickable {
                              val pickImageIntent = Intent(Intent.ACTION_PICK)
                              pickImageIntent.type = "image/*"
                              launcher.launch(pickImageIntent)
                            },
                    contentAlignment = Alignment.Center) {
                      if (event.image == Uri.EMPTY) {
                        Text(text = "Image", modifier = Modifier.align(Alignment.Center))
                      } else {
                        Image(
                            painter = rememberAsyncImagePainter(event.image),
                            contentDescription = "image",
                            modifier =
                                Modifier.size(150.dp)
                                    .background(MaterialTheme.colorScheme.surface)
                                    .testTag("Image")
                                    .clickable {
                                      val pickImageIntent = Intent(Intent.ACTION_PICK)
                                      pickImageIntent.type = "image/*"
                                      launcher.launch(pickImageIntent)
                                    })
                      }
                    }
              }

              item {
                Row(
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                      OutlinedButton(
                          modifier = Modifier.testTag("StartTimePicker"),
                          shape = RoundedCornerShape(8.dp),
                          onClick = { showStartDatePicker = true }) {
                            Text(
                                event.startTime?.format(
                                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
                                    ?: "Start Time Picker",
                                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                                color = Color.Black)
                          }
                      Spacer(modifier = Modifier.width(8.dp))
                      OutlinedButton(
                          modifier = Modifier.testTag("EndTimePicker"),
                          shape = RoundedCornerShape(8.dp),
                          onClick = {
                            if (event.startTime != null) {
                              showEndDatePicker = true
                            } else {
                              Toast.makeText(
                                      context, "Please select start time first", Toast.LENGTH_SHORT)
                                  .show()
                            }
                          },
                          colors = ButtonDefaults.outlinedButtonColors(Color.Transparent)) {
                            Text(
                                event.endTime?.format(
                                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
                                    ?: "End Time Picker",
                                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                                color = Color.Black)
                          }
                    }
              }

              item {
                Button(
                    modifier = Modifier.testTag("CreateButton"),
                    enabled =
                        event.description.isNotEmpty() &&
                            event.image != Uri.EMPTY &&
                            event.title.isNotEmpty() &&
                            event.startTime != null &&
                            event.endTime != null,
                    onClick = {
                      viewModel.createEvent(onSuccess = { navigationActions.goBack() })
                    }) {
                      Text(
                          text = "Create",
                          fontSize = 20.sp,
                          fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)))
                    }
              }
            }
      }
}

private fun convertTo24from(localTime: LocalDateTime, format: HourFormat): LocalDateTime =
    when (format) {
      HourFormat.AM ->
          LocalDateTime.of(
              localTime.toLocalDate(), LocalTime.of(localTime.hour - 12, localTime.minute))
      HourFormat.PM ->
          LocalDateTime.of(localTime.toLocalDate(), LocalTime.of(localTime.hour, localTime.minute))
    }

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onDismissRequest: () -> Unit,
    confirmButton: @Composable (() -> Unit),
    dismissButton: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
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
              modifier = Modifier.padding(24.dp),
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
