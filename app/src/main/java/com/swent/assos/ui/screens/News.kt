package com.swent.assos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.data.News
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.NewsViewModel
import com.swent.assos.ui.components.HomeItem
import com.swent.assos.ui.theme.Typography

@Composable
fun News(navigationActions: NavigationActions) {
  val viewModel: NewsViewModel = hiltViewModel()
  val news by viewModel.allNews.collectAsState()
  val listState = rememberLazyListState()

  LaunchedEffect(listState) {
    snapshotFlow { listState.layoutInfo.visibleItemsInfo }
        .collect { visibleItems ->
          if (visibleItems.isNotEmpty() && visibleItems.last().index == news.size - 1) {
            viewModel.loadMoreAssociations()
          }
        }
  }

  Scaffold(
      modifier = Modifier.testTag("NewsScreen").padding(top = 30.dp),
      topBar = { HomePageTitle() }) { paddingValues ->
        LazyColumn(
            modifier =
                Modifier.padding(paddingValues)
                    .background(MaterialTheme.colorScheme.outline) // Light gray background
                    .padding(horizontal = 15.dp)
                    .padding(top = 5.dp)
                    .testTag("NewsList"),
            userScrollEnabled = true,
            state = listState) {
              items(news) { newsItem ->
                HomeItemWrapper(newsItem, navigationActions)
                Spacer(modifier = Modifier.height(15.dp))
              }
            }
      }
}

@Composable
fun HomeItemWrapper(news: News, navigationActions: NavigationActions) {
  // Clickable modifier to handle user interactions
  Box(
      modifier =
          Modifier.fillMaxWidth().testTag("NewsListItem").clickable {
            val dest =
                Destinations.NEWS_DETAILS.route +
                    "/${
                              news.id
                          }"
            navigationActions.navigateTo(dest)
          }) {
        HomeItem(news = news, navigationActions)
      }
}

@Composable
fun HomePageTitle() {
  Box(
      modifier =
          Modifier.width(361.dp)
              .height(116.dp)
              .background(MaterialTheme.colorScheme.onPrimary)
              .padding(bottom = 24.dp),
  ) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start) {
          Text(
              text = "Home",
              style =
                  Typography.bodyLarge.copy(
                      fontWeight = FontWeight.Bold, // Adjust the weight as needed
                      fontSize = 30.sp,
                      color = MaterialTheme.colorScheme.onBackground),
              modifier = Modifier.align(Alignment.CenterHorizontally))
        }
  }
}
