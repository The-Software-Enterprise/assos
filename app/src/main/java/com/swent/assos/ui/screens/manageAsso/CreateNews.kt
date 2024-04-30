package com.swent.assos.ui.screens.manageAsso

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.CreateNewsViewModel
import com.swent.assos.ui.components.PageTitleWithGoBack

@OptIn(ExperimentalMaterial3Api::class)
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
      modifier = Modifier
          .fillMaxSize()
          .testTag("CreateNewsScreen"),
      topBar = {PageTitleWithGoBack(title = "Create a news", navigationActions = navigationActions)},
      floatingActionButton = {
        FloatingActionButton(
            modifier = Modifier.testTag("AddImages"),
            onClick = { showAddImages = true },
            shape = RoundedCornerShape(size = 16.dp)) {
              Image(imageVector = Icons.Default.Add, contentDescription = null)
            }
      }) { paddingValues ->
        LazyColumn(
            modifier =
            Modifier
                .padding(top = 10.dp, start = 26.dp, end = 26.dp)
                .fillMaxSize()
                .testTag("Form"),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = paddingValues,
        ) {
          item {
            OutlinedTextField(
                value = news.title,
                onValueChange = { viewModel.setTitle(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .testTag("InputTitle"),
                label = { Text(text = "Title") },
                placeholder = { Text(text = "Title of the news") },
                textStyle = MaterialTheme.typography.bodyLarge,
                colors =
                    TextFieldDefaults.colors(
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    ),
                singleLine = true,
            )
          }
          item {
            OutlinedTextField(
                value = news.description,
                onValueChange = { viewModel.setDescription(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .testTag("InputDescription"),
                label = { Text(text = "Description") },
                placeholder = { Text(text = "Description of the news") },
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyLarge,
                colors =
                    TextFieldDefaults.colors(
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    ),
            )
          }
          item {
            FilledIconButton(
                onClick = { showImages = true },
                modifier = Modifier
                    .testTag("ShowImages")
                    .width(200.dp)) {
                  Text("Show Images")
                }
          }

          item {
            FilledIconButton(
                onClick = { viewModel.createNews(assoId, navigationActions) },
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = if (isEdit) 0.dp else 40.dp)
                    .height(40.dp)
                    .testTag("ButtonSave"),
                shape = RoundedCornerShape(10.dp),
                enabled = news.title.isNotEmpty() && news.description.isNotEmpty(),
                colors =
                    IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor =
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))) {
                  Text(
                      text = if (isEdit) "Save" else "Create",
                      style = MaterialTheme.typography.labelLarge,
                      color = MaterialTheme.colorScheme.onPrimary)
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
        modifier = Modifier
            .width(400.dp)
            .height(250.dp),
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(size = 8.dp)) {
          Column(
              modifier = Modifier
                  .fillMaxSize()
                  .testTag("AddImageDialog"),
              horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Add Image", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.testTag("InputImage"),
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("Image URL") })
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                  Button(
                      onClick = { onDismissRequest() },
                      modifier = Modifier.testTag("CancelImage")) {
                        Text("Cancel")
                      }
                  Spacer(modifier = Modifier.width(16.dp))
                  Button(
                      onClick = { onConfirmation(url) }, modifier = Modifier.testTag("SaveImage")) {
                        Text("Confirm")
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
    Surface(
        modifier = Modifier
            .width(400.dp)
            .height(350.dp),
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(size = 8.dp)) {
          LazyColumn(modifier = Modifier.testTag("ShowImagesDialog")) {
            items(images) { image ->
              Card(
                  modifier =
                  Modifier
                      .testTag("ImageShown")
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
