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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.NFCWriter
import com.swent.assos.R
import com.swent.assos.model.data.Event
import com.swent.assos.model.generateQRCode
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.saveImageToGallery
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.ui.components.ImageListItem
import com.swent.assos.ui.components.LabeledSwitch

private const val QR_CODE_SIZE = 500

@Composable
fun EventContent(
    viewModel: EventViewModel,
    paddingValues: PaddingValues,
    isEdition: Boolean = false,
    isMember: Boolean = false,
    lazyListState: LazyListState = rememberLazyListState(),
    eventId: String = "",
    navigationActions: NavigationActions
) {
  val event by viewModel.event.collectAsState()
  var fieldIndex by remember { mutableIntStateOf(-1) }
  val context = LocalContext.current

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

  val launcher =
      rememberLauncherForActivityResult(
          contract = ActivityResultContracts.StartActivityForResult(), onResult = {})
  val intent = Intent(LocalContext.current, NFCWriter::class.java).putExtra("eventID", eventId)

  LazyColumn(
      state = lazyListState,
      modifier = Modifier.padding(paddingValues).fillMaxWidth().testTag("EventContent"),
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
                              contentDescription = "",
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

        if (!isEdition && isMember) {
          item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                  Button(
                      modifier =
                          Modifier.testTag("DownloadQRCode")
                              .padding(12.dp)
                              .weight(1f)
                              .height(60.dp),
                      shape = RoundedCornerShape(16.dp),
                      onClick = {
                        saveImageToGallery(
                            context, generateQRCode(event.id, QR_CODE_SIZE).asImageBitmap())
                      }) {
                        Icon(
                            painter = painterResource(id = R.drawable.download_icon),
                            contentDescription = null)
                        Text(
                            "QR Code", modifier = Modifier.padding(start = 12.dp), fontSize = 18.sp)
                      }
                  Button(
                      modifier =
                          Modifier.testTag("SetupNFCTag").padding(12.dp).weight(1f).height(60.dp),
                      shape = RoundedCornerShape(16.dp),
                      onClick = { launcher.launch(intent) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.nfc_icon),
                            contentDescription = null)
                        Text("NFC", modifier = Modifier.padding(start = 12.dp), fontSize = 18.sp)
                      }
                }
          }
          item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                  if (event.isStaffingEnabled) {
                    Button(
                        modifier = Modifier.padding(12.dp).weight(1f).height(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                          navigationActions.navigateTo(
                              Destinations.STAFF_MANAGEMENT.route + "/${eventId}")
                        }) {
                          Icon(
                              painter = painterResource(id = R.drawable.list_icon),
                              contentDescription = null)
                          Text(
                              "Staff List",
                              modifier = Modifier.padding(start = 12.dp),
                              fontSize = 18.sp)
                        }
                  }
                  Button(
                      modifier = Modifier.padding(12.dp).weight(1f).height(60.dp),
                      shape = RoundedCornerShape(16.dp),
                      onClick = {
                        navigationActions.navigateTo(
                            Destinations.CREATE_TICKET.route + "/${eventId}")
                      }) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_icon),
                            contentDescription = null)
                        Text(
                            "Add Ticket",
                            modifier = Modifier.padding(start = 12.dp),
                            fontSize = 18.sp)
                      }
                }
          }
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
                                    contentDescription = "",
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
