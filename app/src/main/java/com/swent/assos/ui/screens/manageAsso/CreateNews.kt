package com.swent.assos.ui.screens.manageAsso

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.CreateNewsViewModel
import com.swent.assos.ui.components.ImageListItem
import com.swent.assos.ui.components.PageTitleWithGoBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNews(
    navigationActions: NavigationActions,
    assoId: String,
    viewModel: CreateNewsViewModel = hiltViewModel(),
    launcher: ManagedActivityResultLauncher<String, List<@JvmSuppressWildcards Uri>> =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
              viewModel.addImages(uris)
            }
) {

  val context = LocalContext.current

  val news by viewModel.news.collectAsState()

  Scaffold(
      modifier = Modifier.fillMaxSize().testTag("CreateNewsScreen"),
      topBar = {
        PageTitleWithGoBack(
            title = "Create news",
            navigationActions = navigationActions,
            actionButton = {
              Box(
                  modifier =
                      Modifier.padding(end = 16.dp)
                          .clip(RoundedCornerShape(20))
                          .background(
                              MaterialTheme.colorScheme.primary.copy(
                                  alpha =
                                      if (news.title.isBlank() ||
                                          news.description.isBlank() ||
                                          news.images.isEmpty())
                                          0.5f
                                      else 1f))
                          .clickable {
                            viewModel.createNews(
                                assoId,
                                onSuccess = {
                                  navigationActions.goBack()
                                  Toast.makeText(
                                          context,
                                          "The news has been successfully created!",
                                          Toast.LENGTH_SHORT)
                                      .show()
                                },
                                onError = {
                                  Toast.makeText(
                                          context,
                                          "Unfortunately, the news could not be created. Please try again!",
                                          Toast.LENGTH_SHORT)
                                      .show()
                                })
                          }
                          .padding(vertical = 5.dp, horizontal = 10.dp)
                          .testTag("CreateButton")) {
                    Text(
                        text = "Publish",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onPrimary)
                  }
            })
      },
      floatingActionButton = {
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.testTag("AddImages"),
            onClick = { launcher.launch("image/*") },
            shape = RoundedCornerShape(size = 16.dp)) {
              Image(
                  imageVector = Icons.Default.Add,
                  contentDescription = null,
                  colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary))
            }
      }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.testTag("Form"),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = paddingValues,
        ) {
          item {
            OutlinedTextField(
                textStyle =
                    TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 32.sp,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        color = MaterialTheme.colorScheme.onBackground),
                value = news.title,
                onValueChange = { viewModel.setTitle(it) },
                modifier =
                    Modifier.padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(64.dp)
                        .testTag("InputTitle"),
                label = { Text(text = "Title") },
                placeholder = { Text(text = "Title of the news") },
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.surface,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.surface,
                        unfocusedLabelColor =
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary,
                        cursorColor = MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
            )
          }

          item {
            OutlinedTextField(
                value = news.description,
                onValueChange = { viewModel.setDescription(it) },
                modifier =
                    Modifier.padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(128.dp)
                        .testTag("InputDescription"),
                label = { Text(text = "Description") },
                placeholder = { Text(text = "Description of the news") },
                singleLine = false,
                textStyle =
                    TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 32.sp,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        color = MaterialTheme.colorScheme.onBackground),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.surface,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.surface,
                        unfocusedLabelColor =
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary,
                        cursorColor = MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(8.dp),
            )
          }

          item {
            if (news.images.isEmpty()) {
              Column(
                  modifier = Modifier.fillMaxSize().padding(top = 50.dp),
                  horizontalAlignment = Alignment.CenterHorizontally,
              ) {
                Text(
                    text = "No images added",
                    modifier = Modifier.testTag("NoImages"),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                    fontSize = 15.sp,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Click on the button below to add images.",
                    modifier = Modifier.testTag("NoImages").width(200.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                )
              }
            } else {
              LazyRow(
                  modifier = Modifier.fillMaxWidth().aspectRatio(1f).padding(top = 10.dp),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { Spacer(modifier = Modifier.width(12.dp)) }
                    items(news.images) { ImageListItem(uri = it) { viewModel.removeImage(it) } }
                    item { Spacer(modifier = Modifier.width(12.dp)) }
                  }
            }
          }
        }
      }
}
