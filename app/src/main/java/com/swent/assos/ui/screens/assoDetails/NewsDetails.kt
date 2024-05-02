package com.swent.assos.ui.screens.assoDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.R
import com.swent.assos.model.data.News
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.model.view.NewsViewModel
import com.swent.assos.ui.theme.Typography
import com.swent.assos.ui.theme.VeryLightGray

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewsDetails(newsId: String, navigationActions: NavigationActions) {
  val viewModel: NewsViewModel = hiltViewModel()
  //val assoViewModel: AssoViewModel = hiltViewModel()

  val news by viewModel.news.collectAsState()
  //val asso by assoViewModel.association.collectAsState()

  LaunchedEffect(key1 = Unit) {
    viewModel.getNews(newsId)
    //assoViewModel.getAssociation(news.associationId)
  }

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("NewsDetailsScreen"),
      topBar = { TopNewsBar(news = news, navigationActions = navigationActions) }) { paddingValues
        ->
        LazyColumn(modifier = Modifier.padding(paddingValues).testTag("Content")) {
          item {
            /*Text(
                text = asso.fullname,
                style =
                    Typography.bodyLarge.copy(
                        fontSize = 1.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 0.25.sp,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        fontWeight = FontWeight.Light),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 0.dp))*/

            if (news.images.isNotEmpty()) {
              Image(
                  painter = rememberAsyncImagePainter(model = news.images[0]),
                  contentDescription = null,
                  modifier =
                      Modifier.fillMaxWidth()
                          .padding(10.dp)
                          .height(200.dp)
                          .background(Color.Gray, shape = RoundedCornerShape(20.dp)),
                  contentScale = ContentScale.Crop,
                  alignment = Alignment.Center)
            }
            Box(
                modifier =
                    Modifier.width(400.dp)
                        .padding(top = 4.dp, bottom = 3.dp)
                        .background(VeryLightGray, shape = RoundedCornerShape(20.dp))) {
                  Text(
                      text = news.description,
                      style =
                          Typography.bodyLarge.copy(
                              fontSize = 14.sp,
                              lineHeight = 20.sp,
                              letterSpacing = 0.25.sp,
                              fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                              fontWeight = FontWeight.Light),
                      modifier =
                          Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 0.dp))
                }
            if (news.images.isNotEmpty()) {
              LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                items(news.images) { image ->
                  Card(
                      modifier =
                          Modifier.padding(10.dp)
                              .width(120.dp)
                              .height(70.dp), // Keep your height as it is
                      shape = RoundedCornerShape(20.dp),
                  ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = image),
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNewsBar(
    news: News,
    navigationActions: NavigationActions,
) {
  MediumTopAppBar(
      colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
      modifier = Modifier.testTag("Header"),
      title = { Text(news.title, modifier = Modifier.testTag("Title")) },
      navigationIcon = {
        Image(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier.testTag("GoBackButton").clickable { navigationActions.goBack() })
      })
}
