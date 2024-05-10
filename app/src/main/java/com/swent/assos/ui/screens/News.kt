package com.swent.assos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.NewsViewModel
import com.swent.assos.ui.components.HomeItem
import com.swent.assos.ui.components.PageTitle

@Composable
fun News(navigationActions: NavigationActions) {
  val viewModel: NewsViewModel = hiltViewModel()
  val news by viewModel.allNews.collectAsState()
  val listState = rememberLazyListState()

  Scaffold(modifier = Modifier.testTag("NewsScreen"), topBar = { PageTitle(title = "Home") }) {
      paddingValues ->
    LazyColumn(
        modifier =
            Modifier.padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 15.dp)
                .padding(vertical = 5.dp)
                .testTag("NewsList"),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        userScrollEnabled = true,
        state = listState) {
          items(news) { news -> HomeItem(news = news, navigationActions) }
        }
  }
}
