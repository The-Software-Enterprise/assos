package com.swent.assos.ui.screens.manageAsso

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.CreateNewsViewModel
import com.swent.assos.ui.components.PageTitleWithGoBack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateNews(navigationActions: NavigationActions, assoId: String, isEdit: Boolean = false) {
  val viewModel: CreateNewsViewModel = hiltViewModel()

  val news by viewModel.news.collectAsState()
  var showAddImages by remember { mutableStateOf(false) }
  var showImages by remember { mutableStateOf(false) }

  if (showAddImages) {
    AddImage(
        onDismissRequest = { showAddImages = false },
        onConfirmation = {
          news.images += it
          showAddImages = false
        })
  }

  if (showImages) {
    ShowImages(images = news.images, onDismissRequest = { showImages = false })
  }
  Scaffold(
      modifier = Modifier.fillMaxSize().semantics { testTagsAsResourceId = true }.testTag("CreateNewsScreen"),
      topBar = {
        PageTitleWithGoBack(title = "Create a news", navigationActions = navigationActions)
      },
  ) { paddingValues ->
    LazyColumn(
        modifier = Modifier.padding(paddingValues).fillMaxWidth().testTag("Form"),
        horizontalAlignment = Alignment.CenterHorizontally) {
          item {
            OutlinedTextField(
                modifier =
                    Modifier.fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 8.dp)
                        .testTag("InputTitle"),
                value = news.title,
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
                value = news.description,
                onValueChange = { viewModel.setDescription(it) },
                textStyle =
                    TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular))),
                label = { Text(text = "Description") },
                singleLine = false,
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary,
                        cursorColor = MaterialTheme.colorScheme.secondary))
          }

          item {
            Row(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                  FloatingActionButton(
                      modifier = Modifier.testTag("AddImages"),
                      onClick = { showAddImages = true },
                      containerColor = MaterialTheme.colorScheme.secondary,
                      shape = RoundedCornerShape(size = 16.dp)) {
                        Image(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary))
                      }
                  Spacer(modifier = Modifier.width(32.dp))
                  FloatingActionButton(
                      modifier = Modifier.testTag("ShowImages").width(130.dp),
                      onClick = { showImages = true },
                      containerColor = MaterialTheme.colorScheme.secondary) {
                        Text(
                            "Show Images",
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)))
                      }
                }
          }

          item {
            Spacer(modifier = Modifier.height(250.dp))
            Button(
                modifier = Modifier.testTag("ButtonSave"),
                enabled = news.title.isNotEmpty() && news.description.isNotEmpty(),
                onClick = { viewModel.createNews(assoId, navigationActions) },
            ) {
              Text(
                  text = "Create",
                  fontSize = 20.sp,
                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)))
            }
          }
        }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddImage(onDismissRequest: () -> Unit, onConfirmation: (String) -> Unit) {

  var url by remember { mutableStateOf("") }

  AlertDialog(onDismissRequest = { onDismissRequest() }) {
    Surface(
        modifier = Modifier.width(400.dp).height(230.dp),
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(size = 8.dp)) {
          Column(
              modifier = Modifier.fillMaxSize().testTag("AddImageDialog"),
              horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Add Image",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)))
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.testTag("InputImage"),
                    value = url,
                    onValueChange = { url = it },
                    label = {
                      Text(
                          "Image URL", fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)))
                    })
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                  Button(
                      onClick = { onDismissRequest() },
                      modifier = Modifier.testTag("CancelImage")) {
                        Text("Cancel", fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)))
                      }
                  Spacer(modifier = Modifier.width(16.dp))
                  Button(
                      onClick = { onConfirmation(url) }, modifier = Modifier.testTag("SaveImage")) {
                        Text(
                            "Confirm", fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)))
                      }
                }
              }
        }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowImages(images: List<String>, onDismissRequest: () -> Unit) {
  AlertDialog(onDismissRequest = { onDismissRequest() }) {
    if (images.isEmpty()) {
      Surface(
          modifier = Modifier.width(100.dp).height(100.dp),
          color = MaterialTheme.colorScheme.background,
          shape = RoundedCornerShape(size = 8.dp)) {
            Text(
                modifier = Modifier.padding(horizontal = 86.dp, vertical = 34.dp).testTag("NoImagesDialog"),
                text = "No images",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)))
          }
    } else {
      Surface(
          modifier = Modifier.width(400.dp).height(350.dp),
          color = MaterialTheme.colorScheme.background,
          shape = RoundedCornerShape(size = 8.dp)) {
            LazyColumn(modifier = Modifier.testTag("ShowImagesDialog")) {
              items(images) { image ->
                Card(
                    modifier =
                        Modifier.testTag("ImageShown")
                            .padding(8.dp)
                            .height(100.dp)
                            .width(100.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp)) {
                      Text(text = image)
                    }
              }
            }
          }
    }
  }
}
