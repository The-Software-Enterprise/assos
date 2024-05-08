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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.swent.assos.ui.components.PageTitle

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewsDetails(newsId: String, navigationActions: NavigationActions) {

  val viewModel: NewsViewModel = hiltViewModel()

  val news by viewModel.allNews.collectAsState()

  LaunchedEffect(key1 = Unit) {
    viewModel.getNews()
  }

  val specificNews = news.find { it.id == newsId }
  if (specificNews != null) {

    Scaffold(
        modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("AssoDetailsScreen"),
        topBar = {  PageTitle(title = specificNews.title)  }) { paddingValues ->
          LazyColumn(modifier = Modifier.padding(paddingValues).testTag("Content")) {
            item {
              Image(
                  painter =
                      if (specificNews.images[0] != Uri.EMPTY) {
                        rememberAsyncImagePainter(specificNews.images[0])
                      } else {
                        painterResource(id = R.drawable.ic_launcher_foreground)
                      },
                  contentDescription = null,
                  modifier =
                      Modifier.fillMaxWidth()
                          .padding(10.dp)
                          .height(200.dp)
                          .clip(shape = RoundedCornerShape(20.dp))
                          .background(Color.Gray),
                  contentScale = ContentScale.Crop,
                  alignment = Alignment.Center)

                Box(
                    modifier =
                    Modifier.width(400.dp)
                        .padding(top = 5.dp, bottom = 4.dp)
                        .background(
                            MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))) {
                    Text(
                        text = specificNews.description,
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        fontWeight = FontWeight.Bold)
                }

                if (specificNews.images.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.padding(paddingValues).testTag("subImageList"),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(specificNews.images) { image ->
                            Card(
                                modifier =
                                Modifier.padding(10.dp)
                                    .width(120.dp)
                                    .height(70.dp)
                                    .testTag("subImage"), // Keep your height as it is
                                shape = RoundedCornerShape(20.dp),
                            ) {
                                AsyncImage(
                                    model = image,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxWidth(),
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
  } else {
      Scaffold(
          modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("AssoDetailsScreen"),
          topBar = {  PageTitle(title = "ERROR - NO NEWS FOUND")  }) { paddingValues ->
          LazyColumn(modifier = Modifier.padding(paddingValues).testTag("Content")) {
              item {
                  Image(
                      painter = painterResource(id = R.drawable.ic_launcher_foreground),
                      contentDescription = null,
                      modifier =
                      Modifier.fillMaxWidth()
                          .padding(10.dp)
                          .height(200.dp)
                          .clip(shape = RoundedCornerShape(20.dp))
                          .background(Color.Gray),
                      contentScale = ContentScale.Crop,
                      alignment = Alignment.Center)

                  Box(
                      modifier =
                      Modifier.width(400.dp)
                          .padding(top = 5.dp, bottom = 4.dp)
                          .background(
                              MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp))) {
                      Text(
                          text = "ERROR - NO NEWS FOUND",
                          style = MaterialTheme.typography.headlineMedium,
                          fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                          fontWeight = FontWeight.Bold)
                  }

                  Spacer(modifier = Modifier.height(20.dp))

              }


          }
      }
  }
}
