package com.swent.assos.ui.screens.manageAsso.createEvent

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.model.data.Event
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.ui.components.ImageListItem
import com.swent.assos.ui.components.PageTitleWithGoBack
import com.swent.assos.ui.screens.manageAsso.createEvent.components.DateTimePickers
import com.swent.assos.ui.screens.manageAsso.createEvent.components.FloatingButtons
import com.swent.assos.ui.screens.manageAsso.createEvent.components.Input

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateEvent(
    assoId: String,
    navigationActions: NavigationActions,
    viewModel: EventViewModel = hiltViewModel()
) {
  val context = LocalContext.current

  val event by viewModel.event.collectAsState()
  val canCreate =
      event.description.isNotEmpty() && event.image != Uri.EMPTY && event.title.isNotEmpty()

  var fieldIndex by remember { mutableIntStateOf(-1) }
  val lazyListState = rememberLazyListState()

  val launcherBanner =
      rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result
        ->
        if (result.resultCode == Activity.RESULT_OK) {
          viewModel.setImage(result.data?.data)
        }
      }

  val launcherImagesField =
      rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) {
          uris: List<Uri> ->
        viewModel.addImagesToField(uris, fieldIndex)
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
                              alpha = if (canCreate) 1f else 0.5f))
                      .clickable {
                        if (canCreate)
                            viewModel.createEvent(
                                onSuccess = {
                                  navigationActions.goBack()
                                  Toast.makeText(
                                          context,
                                          "The event has been successfully created!",
                                          Toast.LENGTH_SHORT)
                                      .show()
                                },
                                onError = {
                                  Toast.makeText(
                                          context,
                                          "Unfortunately, the event could not be created. Please try again!",
                                          Toast.LENGTH_SHORT)
                                      .show()
                                })
                      }
                      .padding(vertical = 5.dp, horizontal = 10.dp)
                      .testTag("CreateButton")) {
                Text(
                    text = "Create",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onPrimary)
              }
        }
      },
      floatingActionButton = {
        FloatingButtons(viewModel = viewModel, lazyListState = lazyListState)
      },
  ) { paddingValues ->
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.padding(paddingValues).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
          item {
            Input(
                viewModel = viewModel,
                value = event.title,
                onValueChange = { viewModel.setTitle(it) },
                placeholder = "Title of the event",
                fontSize = 20.sp,
                lineHeight = 20.sp,
                singleLine = true,
                paddingValues = PaddingValues(horizontal = 16.dp),
                testTag = "InputTitle")
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
                          launcherBanner.launch(pickImageIntent)
                        },
                contentAlignment = Alignment.Center) {
                  when (event.image) {
                    Uri.EMPTY -> {
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
                    }
                    else -> {
                      Image(
                          modifier = Modifier.fillMaxSize().testTag("ImageBanner"),
                          painter = rememberAsyncImagePainter(event.image),
                          contentDescription = "image",
                          contentScale = ContentScale.Crop)
                    }
                  }

                  Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
                    DateTimePickers(viewModel = viewModel)
                  }
                }
          }

          item {
            Input(
                viewModel = viewModel,
                value = event.description,
                onValueChange = { viewModel.setDescription(it) },
                placeholder = "Here, write a short description of the event...",
                fontSize = 13.sp,
                lineHeight = 15.sp,
                singleLine = false,
                paddingValues = PaddingValues(horizontal = 16.dp),
                testTag = "InputDescription",
            )
          }

          itemsIndexed(event.fields, key = { index, _ -> index }) { index, field ->
            when (field) {
              is Event.Field.Text -> {
                Input(
                    viewModel = viewModel,
                    value = field.title,
                    onValueChange = { viewModel.updateFieldTitle(index, it) },
                    placeholder = "Title",
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    singleLine = true,
                    paddingValues = PaddingValues(horizontal = 10.dp),
                    testTag = "InputFieldTitle" + index,
                )
                Box(
                    modifier =
                        Modifier.height(2.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(color = MaterialTheme.colorScheme.surface))
                Input(
                    viewModel = viewModel,
                    value = field.text,
                    onValueChange = { viewModel.updateFieldText(index, it) },
                    placeholder = "Here, write a short description of the event...",
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    singleLine = false,
                    paddingValues = PaddingValues(horizontal = 16.dp),
                    testTag = "InputFieldDescription" + index,
                )
              }
              is Event.Field.Image -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f).padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                      item {
                        Row(
                            modifier =
                                Modifier.fillMaxHeight(0.9f)
                                    .padding(start = 10.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .clickable {
                                      fieldIndex = index
                                      launcherImagesField.launch("image/*")
                                    }
                                    .testTag("InputFieldImage" + index),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center) {
                              Row(
                                  modifier = Modifier.padding(horizontal = 30.dp),
                                  verticalAlignment = Alignment.CenterVertically,
                                  horizontalArrangement = Arrangement.Center,
                              ) {
                                Image(
                                    imageVector = Icons.Outlined.AddPhotoAlternate,
                                    contentDescription = "add an image",
                                    colorFilter =
                                        ColorFilter.tint(MaterialTheme.colorScheme.onSurface))
                              }
                            }
                      }
                      items(field.uris) {
                        ImageListItem(uri = it) { viewModel.removeImageFromField(index, it) }
                      }
                      item { Spacer(modifier = Modifier.width(12.dp)) }
                    }
              }
            }
          }

          item { Spacer(modifier = Modifier.height(30.dp)) }
        }
  }
}
