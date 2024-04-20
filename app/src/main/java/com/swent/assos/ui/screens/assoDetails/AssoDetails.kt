package com.swent.assos.ui.screens.assoDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.ui.components.EventItem
import com.swent.assos.ui.components.NewsItem

@Composable
fun AssoDetails(assoId: String, navigationActions: NavigationActions) {
  val viewModel: AssoViewModel = hiltViewModel()

  val currentUser by DataCache.currentUser.collectAsState()

  val association by viewModel.association.collectAsState()
  val news by viewModel.news.collectAsState()
  val events by viewModel.events.collectAsState()

  val listStateNews = rememberLazyListState()
  val listStateEvents = rememberLazyListState()

  LaunchedEffect(key1 = Unit) {
    viewModel.getAssociation(assoId)
    viewModel.getNews(assoId)
    viewModel.getEvents(assoId)
  }

  LaunchedEffect(listStateNews) {
    snapshotFlow { listStateNews.layoutInfo.visibleItemsInfo }
        .collect { visibleItems ->
          if (visibleItems.isNotEmpty() && visibleItems.last().index == news.size - 1) {
            viewModel.getMoreNews(assoId)
          }
        }
  }

  LaunchedEffect(listStateEvents) {
    snapshotFlow { listStateEvents.layoutInfo.visibleItemsInfo }
        .collect { visibleItems ->
          if (visibleItems.isNotEmpty() && visibleItems.last().index == events.size - 1) {
            viewModel.getMoreEvents(assoId)
          }
        }
  }

  LazyColumn(
      modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        item {
          Row(
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(horizontal = 16.dp, vertical = 8.dp)
                      .testTag("Header"),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween) {
                Image(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier =
                        Modifier.testTag("GoBackButton").clickable { navigationActions.goBack() })
                Text(
                    modifier = Modifier.testTag("Title"),
                    text = association.acronym,
                    style = MaterialTheme.typography.headlineMedium)
                Button(
                    modifier = Modifier.testTag("FollowButton"),
                    onClick = {
                      if (currentUser.following.contains(assoId))
                          viewModel.unfollowAssociation(association.id)
                      else viewModel.followAssociation(association.id)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                      Text(
                          modifier = Modifier.testTag("TextFollowButton"),
                          text =
                              if (currentUser.following.contains(assoId)) "Unfollow" else "Follow",
                          color = Color.White)
                    }
              }
        }

        item {
          Text(
              text = association.description,
              style = MaterialTheme.typography.bodyMedium,
              modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp))
        }

        item {
          Text(
              text = "Upcoming Events",
              style = MaterialTheme.typography.headlineMedium,
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        }

        item {
          if (events.isNotEmpty()) {
            LazyRow(
                state = listStateEvents,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                  items(events) {
                    EventItem(it, navigationActions)
                    Spacer(modifier = Modifier.width(8.dp))
                  }
                }
          } else {
            Text(
                text = "No upcoming events",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
          }
        }

        item {
          Text(
              text = "Latest Posts",
              style = MaterialTheme.typography.headlineMedium,
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        }

        item {
          if (news.isNotEmpty()) {
            LazyRow(
                state = listStateNews,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                  items(news) {
                    NewsItem(it, navigationActions)
                    Spacer(modifier = Modifier.width(8.dp))
                  }
                }
          } else {
            Text(
                text = "No latest posts",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
          }
        }
      }
}
