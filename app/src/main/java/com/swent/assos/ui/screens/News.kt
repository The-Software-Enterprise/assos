package com.swent.assos.ui.screens

import com.swent.assos.ui.components.NewsItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.News
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.NewsViewModel
import com.swent.assos.ui.components.HomeItem
import com.swent.assos.ui.theme.Purple40
import com.swent.assos.ui.theme.Purple80
import com.swent.assos.ui.theme.Typography

@OptIn(ExperimentalComposeUiApi::class)
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
      modifier = Modifier
          .testTag("NewsScreen")
          .padding(top = 30.dp),
      topBar = {
          HomePageTitle()
      }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color(0xFFF0F0F0)) // Light gray background
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
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigationActions.navigateTo("")
            }
    ) {
        HomeItem(news = news, navigationActions)
    }
}

@Composable
fun HomePageTitle() {
    Box(
        modifier = Modifier
            .width(360.dp)
            .height(116.dp)
            .background(Color.White)
            .padding(bottom = 24.dp),
        ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "HOME",
                style = Typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold, // Adjust the weight as needed
                    fontSize = 30.sp,
                    color = Color(0xFF1D1B20)
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}