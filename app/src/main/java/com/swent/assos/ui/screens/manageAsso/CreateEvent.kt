package com.swent.assos.ui.screens.manageAsso

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.EventField
import com.swent.assos.model.data.EventFieldImage
import com.swent.assos.model.data.EventFieldText
import com.swent.assos.model.data.EventFieldType
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import java.time.LocalDateTime
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyColumnState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CreateEvent(assoId: String, navigationActions: NavigationActions) {

  val viewModel: AssoViewModel = hiltViewModel()

  val listFields = remember { mutableStateListOf<EventField>() }
  var openAlertDialogAddFields by remember { mutableStateOf(false) }
  var openAlertDialogFields by remember { mutableStateOf(false) }
  var currentFieldType: EventFieldType by remember { mutableStateOf(EventFieldType.TEXT) }
  /*TODO: */
  var titleEvent by remember { mutableStateOf("") }
  /*TODO : Implemnt Date Picker*/
  var date by remember { mutableStateOf("") }

  if (openAlertDialogAddFields) {
    AlertDialogAddFields(
        onDismissRequest = { openAlertDialogAddFields = false },
        onConfirmation = {
          listFields += it
          openAlertDialogAddFields = false
        },
        onChipClick = {
          currentFieldType =
              EventFieldType.entries[(currentFieldType.ordinal + 1) % EventFieldType.entries.size]
        },
        currentFieldType = currentFieldType)
  }

  if (openAlertDialogFields) {
    AlertDialogFields(
        onDismissRequest = { openAlertDialogFields = false },
        onConfirmation = { openAlertDialogFields = false },
        listFields = listFields
    )
  }

  Scaffold(
      topBar = {
        TopAppBar(
            title = { Text("Create an event") },
            navigationIcon = {
              Image(
                  imageVector = Icons.Default.ArrowBack,
                  contentDescription = null,
                  modifier =
                  Modifier
                    .testTag("GoBackButton")
                    .clickable { navigationActions.goBack() })
            },
            colors =
                TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
        )
      },
      floatingActionButton = {
        FloatingActionButton(
            onClick = { openAlertDialogAddFields = true }, shape = RoundedCornerShape(size = 16.dp)) {
              Image(imageVector = Icons.Default.Add, contentDescription = null)
            }
      }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
              .padding(paddingValues)
              .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
              if (listFields.isNotEmpty()) {
                item {
                  Spacer(modifier = Modifier.height(16.dp))
                  Button(
                      onClick = {
                        val event =
                            Event(
                                /*TODO*/
                                id = "",
                                title = titleEvent,
                                associationId = assoId,
                                image = "",
                                description = "",
                                date = "",
                                startTime = LocalDateTime.now(),
                                endTime = LocalDateTime.now()
                                /*TODO*/
                                )
                        viewModel.createEvent(
                            associationId = assoId,
                            event = event,
                            onSuccess = { navigationActions.goBack() })
                      }) {
                        Text("Validate event")
                      }
                }
              }
              item {
                Button(onClick = { openAlertDialogFields = true }) {
                  Image(
                      painter = painterResource(id = R.drawable.rounded_stacks_24),
                      contentDescription = null,
                      colorFilter = ColorFilter.tint(Color.White))
                }
              }
            }
      }
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
      Log.d("CreateEvent", "from: ${from.index}, to: ${to.index}")
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
                  Modifier
                    .semantics {
                      customActions =
                        listOf(
                          CustomAccessibilityAction(
                            label = "Move Up",
                            action = {
                              Log.d("CreateEvent", "Move up")
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
                              Log.d("CreateEvent", "Move down")
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
