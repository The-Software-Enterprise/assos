package com.swent.assos.ui.screens.manageAsso

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.model.data.Event
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.EventViewModel
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
  val context = LocalContext.current

  val viewModel: EventViewModel = hiltViewModel()

  val event by viewModel.event.collectAsState()

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

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("CreateEventScreen"),
      topBar = {
        PageTitleWithGoBack(title = "Create an event", navigationActions = navigationActions) {
          Box(
              modifier =
                  Modifier.padding(end = 16.dp)
                      .clip(RoundedCornerShape(20))
                      .background(
                          MaterialTheme.colorScheme.primary.copy(
                              alpha =
                                  if (event.description.isNotEmpty() &&
                                      event.image != Uri.EMPTY &&
                                      event.title.isNotEmpty())
                                      1f
                                  else 0.5f))
                      .clickable {
                        if (event.description.isNotEmpty() &&
                            event.image != Uri.EMPTY &&
                            event.title.isNotEmpty())
                            viewModel.createEvent(onSuccess = { navigationActions.goBack() })
                      }
                      .padding(vertical = 5.dp, horizontal = 10.dp)
                      .testTag("CreateButton")) {
                Text(
                    text = "Publish",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onPrimary)
              }
        }
      },
      floatingActionButton = {
        Column {
          FloatingActionButton(
              modifier = Modifier.testTag("AddTextField"),
              onClick = { viewModel.addField(Event.Field.Text("", "")) },
              shape = RoundedCornerShape(size = 16.dp)) {
                Image(imageVector = Icons.Default.TextFields, contentDescription = null)
              }
          Spacer(modifier = Modifier.size(16.dp))
          FloatingActionButton(
              modifier = Modifier.testTag("AddImageField"),
              onClick = { viewModel.addField(Event.Field.Image(emptyList())) },
              shape = RoundedCornerShape(size = 16.dp)) {
                Image(imageVector = Icons.Default.PhotoLibrary, contentDescription = null)
              }
        }
      },
  ) { paddingValues ->
    LazyColumn(
        modifier = Modifier.padding(paddingValues).fillMaxWidth().testTag("Form"),
        horizontalAlignment = Alignment.CenterHorizontally) {
          item {
            OutlinedTextField(
                modifier =
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("InputTitle"),
                value = event.title,
                onValueChange = { viewModel.setTitle(it) },
                textStyle = TextStyle(fontSize = 20.sp),
                placeholder = {
                  Text(
                      "Title of the event",
                      fontSize = 20.sp,
                      color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f))
                },
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.background,
                        unfocusedBorderColor = MaterialTheme.colorScheme.background,
                        cursorColor = MaterialTheme.colorScheme.secondary,
                    ),
                singleLine = true)
          }

          item {
            Box(
                modifier =
                    Modifier.fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .height(200.dp)
                        .shadow(elevation = 3.dp, shape = RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .testTag("InputImage")
                        .clickable {
                          val pickImageIntent = Intent(Intent.ACTION_PICK)
                          pickImageIntent.type = "image/*"
                          launcher.launch(pickImageIntent)
                        },
                contentAlignment = Alignment.Center) {
                  if (event.image == Uri.EMPTY) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(bottom = 40.dp)) {
                          Image(
                              imageVector = Icons.Outlined.AddPhotoAlternate,
                              contentDescription = "add an image",
                              colorFilter =
                                  ColorFilter.tint(
                                      MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)))
                          Spacer(modifier = Modifier.width(10.dp))
                          Text(
                              "add an image",
                              fontSize = 15.sp,
                              color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                        }
                  } else {
                    Image(
                        painter = rememberAsyncImagePainter(event.image),
                        contentDescription = "image",
                        modifier =
                            Modifier.fillMaxSize().testTag("Image").clickable {
                              val pickImageIntent = Intent(Intent.ACTION_PICK)
                              pickImageIntent.type = "image/*"
                              launcher.launch(pickImageIntent)
                            },
                        contentScale = ContentScale.Crop)
                  }

                  Row(
                      modifier =
                          Modifier.fillMaxWidth()
                              .align(Alignment.BottomCenter)
                              .padding(10.dp)
                              .clip(RoundedCornerShape(10.dp))
                              .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
                              .height(35.dp),
                      horizontalArrangement = Arrangement.SpaceEvenly,
                      verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier =
                                Modifier.testTag("StartTimePicker")
                                    .fillMaxSize()
                                    .weight(1f)
                                    .clickable { showStartDatePicker = true },
                            contentAlignment = Alignment.Center) {
                              Text(
                                  event.startTime.format(
                                      DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                                  fontSize = 13.sp,
                                  color = MaterialTheme.colorScheme.onBackground)
                            }
                        Box(
                            modifier =
                                Modifier.width(1.dp)
                                    .fillMaxHeight()
                                    .background(
                                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)))
                        Box(
                            modifier =
                                Modifier.testTag("EndTimePicker")
                                    .fillMaxSize()
                                    .weight(1f)
                                    .clickable { showEndDatePicker = true },
                            contentAlignment = Alignment.Center) {
                              Text(
                                  event.endTime.format(
                                      DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                                  fontSize = 13.sp,
                                  color = MaterialTheme.colorScheme.onBackground)
                            }
                      }
                }
          }

          item {
            OutlinedTextField(
                modifier =
                    Modifier.fillMaxWidth()
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp)
                        .testTag("InputDescription"),
                value = event.description,
                onValueChange = { viewModel.setDescription(it) },
                singleLine = false,
                textStyle = TextStyle(fontSize = 13.sp, lineHeight = 15.sp),
                placeholder = {
                  Text(
                      "Here, write a short description of the event...",
                      fontSize = 13.sp,
                      lineHeight = 15.sp,
                      color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f))
                },
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.secondary,
                    ))
          }

          items(event.fields) {
            if (it is Event.Field.Text) {
              OutlinedTextField(
                  modifier =
                      Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("InputText"),
                  value = it.text,
                  onValueChange = {},
                  textStyle = TextStyle(fontSize = 13.sp),
                  placeholder = {
                    Text(
                        "Here, write a short description of the event...",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f))
                  },
                  colors =
                      OutlinedTextFieldDefaults.colors(
                          focusedBorderColor = MaterialTheme.colorScheme.background,
                          unfocusedBorderColor = MaterialTheme.colorScheme.background,
                          cursorColor = MaterialTheme.colorScheme.secondary,
                      ),
                  singleLine = true)
            } else if (it is Event.Field.Image) {
              LazyRow(
                  modifier = Modifier.fillMaxWidth().aspectRatio(1f).padding(top = 10.dp),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { Spacer(modifier = Modifier.width(12.dp)) }
                    items(it.uris) {
                      Box(
                          modifier =
                              Modifier.fillMaxHeight(0.9f)
                                  .aspectRatio(1f)
                                  .clip(RoundedCornerShape(8.dp))
                                  .background(Color.Gray),
                      ) {
                        Image(
                            painter = rememberAsyncImagePainter(it),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().align(Alignment.Center),
                        )
                        Image(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = "Trash",
                            modifier =
                                Modifier.align(Alignment.TopEnd)
                                    .padding(6.dp)
                                    .size(30.dp)
                                    .background(
                                        MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                                        RoundedCornerShape(5.dp))
                                    .clickable {}
                                    .padding(3.dp),
                            colorFilter =
                                ColorFilter.tint(
                                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)),
                        )
                      }
                    }
                    item {
                      Box(
                          modifier =
                              Modifier.fillMaxHeight(0.9f)
                                  .aspectRatio(1f)
                                  .clip(RoundedCornerShape(8.dp))
                                  .background(Color.Gray),
                          contentAlignment = Alignment.Center) {
                            FloatingActionButton(
                                onClick = {}, shape = RoundedCornerShape(size = 8.dp)) {
                                  Image(
                                      imageVector = Icons.Default.AddPhotoAlternate,
                                      contentDescription = "add an image")
                                }
                          }
                    }
                    item { Spacer(modifier = Modifier.width(12.dp)) }
                  }
            }
          }
        }
  }

  if (showStartDatePicker) {
    DatePickerDialog(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
        onDismissRequest = {},
        confirmButton = {
          TextButton(
              onClick = {
                val selectedDate =
                    startDatePickerState.selectedDateMillis?.let {
                      Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                val currentDate = LocalDate.now()
                if (selectedDate != null) {
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
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
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
                if (selectedEndDate != null) {
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
