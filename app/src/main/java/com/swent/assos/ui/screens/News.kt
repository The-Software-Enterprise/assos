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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.model.view.NewsViewModel
import com.swent.assos.ui.components.HomeItem
import com.swent.assos.ui.components.PageTitle
import java.time.LocalDateTime
import java.time.ZoneOffset

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun News(navigationActions: NavigationActions) {
  val viewModel: NewsViewModel = hiltViewModel()
  val followingNews by viewModel.allNews.collectAsState()
  val allNews by viewModel.allNewsOfAllAssos.collectAsState()

  val eventsViewModel: EventViewModel = hiltViewModel()
  val followingEvents by eventsViewModel.events.collectAsState()
  val allEvents by eventsViewModel.eventsOfAllAssociations.collectAsState()

  val listState = rememberLazyListState()
  val loading = viewModel.loading.collectAsState()

  LaunchedEffect(key1 = Unit) {
    viewModel.loadNews()
    viewModel.loadAllNews()
    eventsViewModel.loadAllEvents()
    eventsViewModel.getEventsForCurrentUser()
  }

  val homeItemsFollowing = combineAndSortItems(followingEvents, followingNews)
  val homeItemsAllAssociation = combineAndSortItems(allEvents, allNews)

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
          /*if (loading.value) {
            item { LoadingCircle() }
          } else {*/
          if (followingNews.isNotEmpty() || followingEvents.isNotEmpty())
              item {
                Text(
                    text = "Activity of associations you follow :",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 20.sp,
                    modifier = Modifier.testTag("followActivityTitle"))
              }
          items(homeItemsFollowing) { item ->
            when (item) {
              is HomeItemType.EventHomeItem ->
                  HomeItem(id = item.event.id, false, navigationActions)
              is HomeItemType.NewsHomeItem -> HomeItem(id = item.news.id, true, navigationActions)
            }
          }
          item { Spacer(modifier = Modifier.height(10.dp)) }
          // }
          item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Activity from all the associations :",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 20.sp,
                modifier = Modifier.testTag("allActivityTitle"))
          }

          items(homeItemsAllAssociation) { item ->
            when (item) {
              is HomeItemType.EventHomeItem ->
                  HomeItem(id = item.event.id, false, navigationActions)
              is HomeItemType.NewsHomeItem -> HomeItem(id = item.news.id, true, navigationActions)
            }
          }
          item { Spacer(modifier = Modifier.height(10.dp)) }
        }
  }
}

sealed class HomeItemType {
  data class EventHomeItem(val event: Event) : HomeItemType()

  data class NewsHomeItem(val news: News) : HomeItemType()
}

fun combineAndSortItems(events: List<Event>, news: List<News>): List<HomeItemType> {
  val currentTime = LocalDateTime.now()

    val eventss = events.filter { it.startTime.isAfter(currentTime) }

  // Convert both lists to a common list of HomeItemType
  val combinedItems =
      eventss.map { HomeItemType.EventHomeItem(it) } + news.map { HomeItemType.NewsHomeItem(it) }

  // Sort the combined list by the required time criteria
  return combinedItems.sortedBy {
    when (it) {
      is HomeItemType.EventHomeItem ->
          Math.abs(
              it.event.startTime.toEpochSecond(ZoneOffset.UTC) -
                  currentTime.toEpochSecond(ZoneOffset.UTC))
      is HomeItemType.NewsHomeItem ->
          Math.abs(
              it.news.createdAt.toEpochSecond(ZoneOffset.UTC) -
                  currentTime.toEpochSecond(ZoneOffset.UTC))
    }
  }
}
