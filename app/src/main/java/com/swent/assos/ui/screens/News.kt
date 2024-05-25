package com.swent.assos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.NewsViewModel
import com.swent.assos.ui.components.HomeItem
import com.swent.assos.ui.components.LoadingCircle
import com.swent.assos.ui.components.PageTitle

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun News(navigationActions: NavigationActions) {
  val viewModel: NewsViewModel = hiltViewModel()
  val followingNews by viewModel.allNews.collectAsState()
  val allNews by viewModel.allNewsOfAllAssos.collectAsState()

  val listState = rememberLazyListState()
  val loading = viewModel.loading.collectAsState()

  LaunchedEffect(key1 = Unit) {
    viewModel.loadNews()
    viewModel.loadAllNews()
  }

  Scaffold(modifier = Modifier.testTag("NewsScreen"), topBar = { PageTitle(title = "Home") }) {
      paddingValues ->
    LazyColumn(
        modifier =
            Modifier.padding(paddingValues)
                .padding(horizontal = 15.dp)
                .padding(vertical = 5.dp)
                .testTag("NewsList"),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        userScrollEnabled = true,
        state = listState) {
          if (loading.value) {
            item { LoadingCircle() }
          } else {
            if (followingNews.isNotEmpty())
                item {
                  Text(
                      text = "Activity of associations you follow :",
                      style = MaterialTheme.typography.headlineMedium,
                      fontSize = 20.sp,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      color = MaterialTheme.colorScheme.onBackground,
                      modifier = Modifier.testTag("followActivityTitle"))
                }
            items(followingNews) { news ->
              HomeItem(id = news.id, navigationActions = navigationActions)
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item {
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                  text = "Activity from all the associations :",
                  style = MaterialTheme.typography.headlineMedium,
                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                  color = MaterialTheme.colorScheme.onBackground,
                  fontSize = 20.sp,
                  modifier = Modifier.testTag("allActivityTitle"))
            }

            items(allNews) { news -> HomeItem(id = news.id, navigationActions = navigationActions) }
            item { Spacer(modifier = Modifier.height(10.dp)) }
          }
        }
  }
}
