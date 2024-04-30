package com.swent.assos.ui.screens.manageAsso

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.date_time.DateTimeDialog
import com.maxkeppeler.sheets.date_time.models.DateTimeSelection
import com.swent.assos.R
import com.swent.assos.model.data.EventField
import com.swent.assos.model.data.EventFieldImage
import com.swent.assos.model.data.EventFieldText
import com.swent.assos.model.data.EventFieldType
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.model.view.HourFormat
import com.swent.assos.ui.components.PageTitleWithGoBack
import com.swent.assos.ui.theme.blue
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyColumnState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CreateEvent(assoId: String, navigationActions: NavigationActions) {
  val viewModel: EventViewModel = hiltViewModel()

  val event by viewModel.event.collectAsState()
  val currentFieldType by viewModel.fieldType.collectAsState()
  val hourFormat by viewModel.hourFormat.collectAsState()
  val listFieldsReordered = remember { mutableStateListOf<EventField>() }

  var openAlertDialogAddFields by remember { mutableStateOf(false) }
  var openAlertDialogFields by remember { mutableStateOf(false) }

  var showTimePickerStart by remember { mutableStateOf(false) }
  var showTimePickerEnd by remember { mutableStateOf(false) }

  val launcher =
      rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result
        ->
        if (result.resultCode == Activity.RESULT_OK) {
          viewModel.setImage(result.data?.data)
        }
      }

  LaunchedEffect(key1 = Unit) { event.associationId = assoId }

  if (showTimePickerStart) {
    DateTimeDialog(
        state =
            rememberUseCaseState(visible = true, onCloseRequest = { showTimePickerStart = false }),
        selection =
            DateTimeSelection.DateTime(
                selectedDate = event.startTime?.toLocalDate(),
                extraButton = SelectionButton(hourFormat.name),
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

  if (openAlertDialogAddFields) {
    AlertDialogAddFields(
        onDismissRequest = { openAlertDialogAddFields = false },
        onConfirmation = {
          event.fields += it
          listFieldsReordered += it
          openAlertDialogAddFields = false
        },
        onChipClick = { viewModel.switchFieldType() },
        currentFieldType = currentFieldType)
  }

  if (openAlertDialogFields) {
    AlertDialogFields(
        onDismissRequest = {
          listFieldsReordered.clear()
          listFieldsReordered.addAll(event.fields)
          openAlertDialogFields = false
        },
        onConfirmation = {
          event.fields.clear()
          event.fields.addAll(listFieldsReordered)
          openAlertDialogFields = false
        },
        listFields = listFieldsReordered)
  }

  Scaffold(
      modifier = Modifier
          .semantics { testTagsAsResourceId = true }
          .testTag("CreateEventScreen"),
      topBar = {
        PageTitleWithGoBack(title = "Create an event", navigationActions = navigationActions)
      }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .testTag("ContentSection"),
            horizontalAlignment = Alignment.CenterHorizontally) {
              item {
                AddContent(event.title, { viewModel.setTitle(it) }, "Title")
                AddContent(event.description, { viewModel.setDescription(it) }, "Description")

                Image(
                    painter = rememberAsyncImagePainter(event.image),
                    contentDescription = "image",
                    modifier =
                    Modifier
                        .size(100.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable {
                            val pickImageIntent = Intent(Intent.ACTION_PICK)
                            pickImageIntent.type = "image/*"
                            launcher.launch(pickImageIntent)
                        })
              }

              item {
                Row(
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                      OutlinedButton(
                          colors = ButtonDefaults.outlinedButtonColors(
                              containerColor = Color.Blue,
                              disabledContainerColor = Color.Gray
                          ),
                          shape = RoundedCornerShape(8.dp),
                          onClick = {
                            viewModel.resetHourFormat()
                            showTimePickerStart = true
                          }
                      ) {
                        Text(
                            event.startTime?.format(
                                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
                                ?: "Start Time",
                            fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                            color = Color.Black)
                      }
                      Spacer(modifier = Modifier.width(32.dp))
                      OutlinedButton(
                          shape = RoundedCornerShape(8.dp),
                          onClick = {
                            viewModel.resetHourFormat()
                            showTimePickerEnd = true
                          },
                          colors = ButtonDefaults.outlinedButtonColors(Color.Transparent)) {
                            Text(
                                event.endTime?.format(
                                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
                                    ?: "End Time",
                                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                                color = Color.Black)
                          }
                    }
              }

              item {
                Row(
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                      FloatingActionButton(
                          onClick = {
                            viewModel.resetFieldType()
                            openAlertDialogAddFields = true
                          },
                          containerColor = blue,
                          shape = RoundedCornerShape(size = 16.dp)) {
                            Image(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(Color.White))
                          }
                      Spacer(modifier = Modifier.width(32.dp))
                      FloatingActionButton(
                          onClick = { openAlertDialogFields = true },
                          containerColor = blue,
                      ) {
                        Image(
                            painter = painterResource(id = R.drawable.rounded_stacks_24),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Color.White))
                      }
                    }
              }

              item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
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
                          fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)))
                    }
              }
            }
      }
}

@Composable
fun AddContent(value: String, onValueChange: (String) -> Unit, title: String) {
  OutlinedTextField(
      modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 32.dp, vertical = 8.dp),
      value = value,
      onValueChange = onValueChange,
      textStyle =
          TextStyle(fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.sf_pro_display_regular))),
      label = { Text(text = title) },
    colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = blue,
        focusedLabelColor = blue,
        cursorColor = blue
    )
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogAddFields(
    onDismissRequest: () -> Unit,
    onConfirmation: (EventField) -> Unit,
    onChipClick: () -> Unit,
    currentFieldType: EventFieldType
) {

  var textValue by remember { mutableStateOf("") }
  var imageUrlValue by remember { mutableStateOf("") }
  var titleSection by remember { mutableStateOf("") }

  AlertDialog(onDismissRequest = { onDismissRequest() }) {
    Surface(
        modifier = Modifier
            .width(400.dp)
            .height(350.dp),
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(size = 8.dp)) {
          Column(
              modifier = Modifier.fillMaxSize(),
              horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("New field", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                AssistChip(
                    leadingIcon = {
                      when (currentFieldType) {
                        EventFieldType.IMAGE ->
                            Image(
                                imageVector = Icons.Outlined.Image,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(Color(0xFFBA1A1A)))
                        EventFieldType.TEXT ->
                            Image(
                                imageVector = Icons.Default.TextFields,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(Color(0xFFFB9905)))
                      }
                    },
                    modifier =
                    Modifier
                        .border(
                            width = 1.dp,
                            color =
                            when (currentFieldType) {
                                EventFieldType.IMAGE -> Color(0xFFBA1A1A)
                                EventFieldType.TEXT -> Color(0xFFFB9905)
                            },
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                        .height(32.dp),
                    onClick = { onChipClick() },
                    label = {
                      when (currentFieldType) {
                        EventFieldType.IMAGE -> Text(text = "Image", color = Color(0xFFBA1A1A))
                        EventFieldType.TEXT -> Text(text = "Text", color = Color(0xFFFB9905))
                      }
                    })

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = titleSection,
                    onValueChange = { titleSection = it },
                    label = { Text("Title") })

                when (currentFieldType) {
                  EventFieldType.IMAGE -> {
                    OutlinedTextField(
                        value = imageUrlValue,
                        onValueChange = { imageUrlValue = it },
                        label = { Text("Image URL") },
                        modifier = Modifier.padding(16.dp))
                  }
                  EventFieldType.TEXT -> {
                    OutlinedTextField(
                        value = textValue,
                        onValueChange = { textValue = it },
                        label = { Text("Text") },
                        modifier = Modifier.padding(16.dp))
                  }
                }
                Row {
                  Button(onClick = { onDismissRequest() }) { Text("Cancel") }
                  Spacer(modifier = Modifier.width(16.dp))
                  Button(
                      onClick = {
                        when (currentFieldType) {
                          EventFieldType.IMAGE -> {
                            onConfirmation(
                                EventFieldImage(title = titleSection, image = imageUrlValue))
                          }
                          EventFieldType.TEXT -> {
                            onConfirmation(EventFieldText(title = titleSection, text = textValue))
                          }
                        }
                      }) {
                        Text("Confirm")
                      }
                }
              }
        }
  }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AlertDialogFields(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    listFields: MutableList<EventField>
) {

  val lazyListState = rememberLazyListState()
  val reorderableLazyColumnState =
      rememberReorderableLazyColumnState(lazyListState) { from, to ->
        listFields.apply { add(to.index - 1, removeAt(from.index - 1)) }
      }
  AlertDialog(onDismissRequest = { onDismissRequest() }) {
    Surface(
        modifier = Modifier
            .width(400.dp)
            .height(350.dp),
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(size = 8.dp)) {
          LazyColumn(
              modifier = Modifier.fillMaxSize(),
              state = lazyListState,
              horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                  Spacer(modifier = Modifier.height(16.dp))
                  Text("Move fields", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                  Spacer(modifier = Modifier.height(16.dp))
                }

                itemsIndexed(listFields, key = { _, item -> item.hashCode() }) { index, field ->
                  ReorderableItem(reorderableLazyColumnState, key = field.hashCode()) {
                    val interactionSource = remember { MutableInteractionSource() }

                    Card(
                        onClick = {},
                        modifier =
                            Modifier.semantics {
                              customActions =
                                  listOf(
                                      CustomAccessibilityAction(
                                          label = "Move Up",
                                          action = {
                                            if (index > 0) {
                                              listFields.apply { add(index - 1, removeAt(index)) }
                                              true
                                            } else {
                                              false
                                            }
                                          }),
                                      CustomAccessibilityAction(
                                          label = "Move Down",
                                          action = {
                                            if (index < listFields.size - 1) {
                                              listFields.apply { add(index + 1, removeAt(index)) }
                                              true
                                            } else {
                                              false
                                            }
                                          }),
                                  )
                            },
                        interactionSource = interactionSource,
                    ) {
                      Row(
                          modifier = Modifier.fillMaxWidth(),
                          horizontalArrangement = Arrangement.SpaceBetween,
                          verticalAlignment = Alignment.CenterVertically) {
                            Text(text = field.title, Modifier.padding(horizontal = 8.dp))
                            IconButton(
                                modifier =
                                Modifier
                                    .draggableHandle(interactionSource = interactionSource)
                                    .clearAndSetSemantics {},
                                onClick = {}) {
                                  Icon(
                                      painterResource(id = R.drawable.menu),
                                      contentDescription = null)
                                }
                          }
                    }
                  }
                }

                if (listFields.isEmpty()) {
                  item { Text(text = "No fields yet, click add button to add some") }
                }
                item {
                  Row {
                    Button(onClick = { onDismissRequest() }) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { onConfirmation() }) { Text("Confirm") }
                  }
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
