package com.swent.assos.ui.screens.manageAsso.createEvent.components

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.model.data.Event
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.ui.components.ImageListItem
import com.swent.assos.ui.components.LabeledSwitch

@Composable
fun EventContent(
    viewModel: EventViewModel,
    paddingValues: PaddingValues,
    isEdition: Boolean = false,
    lazyListState: LazyListState = rememberLazyListState()
) {
  val event by viewModel.event.collectAsState()
  var fieldIndex by remember { mutableIntStateOf(-1) }

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

  LazyColumn(
      state = lazyListState,
      modifier = androidx.compose.ui.Modifier.padding(paddingValues).fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally) {
        if (isEdition) {
          item {
            LabeledSwitch(label = "Ask for staff", checked = event.isStaffingEnabled) {
              viewModel.setStaffingEnabled(it)
            }
          }
        }

        item {
          Input(
              value = event.title,
              onValueChange = { viewModel.setTitle(it) },
              placeholder = "Title of the event",
              fontSize = 20.sp,
              lineHeight = 20.sp,
              singleLine = true,
              paddingValues = PaddingValues(horizontal = 16.dp),
              testTag = "InputTitle",
              enabled = isEdition,
          )
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
                  DateTimePickers(viewModel = viewModel, isEdition = isEdition)
                }
              }
        }

        item {
          Input(
              value = event.description,
              onValueChange = { viewModel.setDescription(it) },
              placeholder = "Here, write a short description of the event...",
              fontSize = 13.sp,
              lineHeight = 15.sp,
              singleLine = false,
              paddingValues = PaddingValues(horizontal = 16.dp),
              testTag = "InputDescription",
              enabled = isEdition,
          )
        }

        itemsIndexed(event.fields, key = { index, _ -> index }) { index, field ->
          when (field) {
            is Event.Field.Text -> {
              Input(
                  value = field.title,
                  onValueChange = { viewModel.updateFieldTitle(index, it) },
                  placeholder = "Title",
                  fontSize = 16.sp,
                  lineHeight = 16.sp,
                  singleLine = true,
                  paddingValues = PaddingValues(horizontal = 10.dp),
                  testTag = "InputFieldTitle" + index,
                  enabled = isEdition,
              )
              Box(
                  modifier =
                      Modifier.height(2.dp)
                          .fillMaxWidth()
                          .padding(horizontal = 16.dp)
                          .background(color = MaterialTheme.colorScheme.surface))
              Input(
                  value = field.text,
                  onValueChange = { viewModel.updateFieldText(index, it) },
                  placeholder = "Here, write a short description of the event...",
                  fontSize = 13.sp,
                  lineHeight = 13.sp,
                  singleLine = false,
                  paddingValues = PaddingValues(horizontal = 16.dp),
                  testTag = "InputFieldDescription" + index,
                  enabled = isEdition,
              )
            }
            is Event.Field.Image -> {
              LazyRow(
                  modifier =
                      Modifier.fillMaxWidth()
                          .aspectRatio(1f)
                          .padding(top = 10.dp)
                          .testTag("ListImagesField$index"),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (isEdition) {
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
                    }
                    items(field.uris) {
                      when (isEdition) {
                        true ->
                            ImageListItem(uri = it) { viewModel.removeImageFromField(index, it) }
                        false -> ImageListItem(uri = it)
                      }
                    }
                    item { Spacer(modifier = Modifier.width(12.dp)) }
                  }
            }
          }
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
      }
}
