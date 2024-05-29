package com.swent.assos.ui.screens.assoDetails

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.R
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.NewsViewModel
import com.swent.assos.model.view.ProfileViewModel
import com.swent.assos.ui.components.DeleteButton
import com.swent.assos.ui.components.PageTitleWithGoBack

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewsDetails(newsId: String, navigationActions: NavigationActions) {

  val viewModel: NewsViewModel = hiltViewModel()
  val specificNews by viewModel.news.collectAsState()
  val profileViewModel: ProfileViewModel = hiltViewModel()
  val associations by profileViewModel.memberAssociations.collectAsState()
  var conf by remember { mutableStateOf(false) }
  LaunchedEffect(key1 = Unit) {
    viewModel.loadNews(newsId)
    profileViewModel.updateUser()
  }
  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("NewsDetailsScreen"),
      floatingActionButton = {
        if (associations.map { it.id }.contains(specificNews.associationId)) {
          DeleteButton { conf = true }
        }
      },
      floatingActionButtonPosition = FabPosition.Center,
      topBar = {
        PageTitleWithGoBack(title = specificNews.title, navigationActions = navigationActions)
      }) { paddingValues ->
        if (conf) {
          ConfirmDialog(
              { conf = false },
              {
                viewModel.deleteNews(newsId)
                navigationActions.goBack()
                conf = false
              },
              specificNews.title)
        }
        LazyColumn(modifier = Modifier.padding(paddingValues).testTag("Content")) {
          item {
            Image(
                painter =
                    if (specificNews.images.isNotEmpty() && specificNews.images[0] != Uri.EMPTY) {
                      rememberAsyncImagePainter(specificNews.images[0])
                    } else {
                      painterResource(id = R.drawable.ic_launcher_foreground)
                    },
                contentDescription = null,
                modifier =
                    Modifier.fillMaxWidth()
                        .padding(15.dp)
                        .height(200.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .testTag("Main Image"),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center)
            Box(
                modifier =
                    Modifier.fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp)
                        .padding(horizontal = 10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(20.dp))
                        .testTag("descriptionBox")) {
                  Text(
                      text = specificNews.description,
                      style = MaterialTheme.typography.bodyMedium,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight.Medium,
                      modifier = Modifier.padding(all = 8.dp).testTag("descriptionText"))
                }

            if (specificNews.images.isNotEmpty()) {
              Text(
                  text = "Pictures :",
                  style = MaterialTheme.typography.headlineSmall,
                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                  fontWeight = FontWeight.Medium,
                  modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

              LazyRow(
                  modifier = Modifier.testTag("subImageList"),
                  contentPadding = PaddingValues(horizontal = 16.dp)) {
                    items(specificNews.images) { image ->
                      Card(
                          modifier =
                              Modifier.padding(horizontal = 10.dp)
                                  .width(150.dp)
                                  .height(120.dp)
                                  .testTag("subImageBox"),
                          shape = RoundedCornerShape(20.dp),
                      ) {
                        AsyncImage(
                            model = image,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().testTag("subImage"),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center)
                        Spacer(modifier = Modifier.width(8.dp))
                      }
                    }
                  }
            }
            Spacer(modifier = Modifier.height(20.dp))
          }
        }
      }
}

@Composable
fun ConfirmDialog(onDismiss: () -> Unit, onConfirm: () -> Unit, title: String) {
  AlertDialog(
      onDismissRequest = onDismiss,
      title = { Text("DELETE") },
      text = { Text("Are you sure to delete $title ?") },
      confirmButton = { Button(onClick = onConfirm) { Text("Yes") } },
      containerColor = MaterialTheme.colorScheme.background,
      dismissButton = { Button(onClick = onDismiss) { Text("No") } })
}
