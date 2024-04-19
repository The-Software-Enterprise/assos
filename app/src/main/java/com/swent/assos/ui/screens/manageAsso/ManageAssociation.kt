package com.swent.assos.ui.screens.manageAsso

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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.data.Association
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.ui.components.EventItem
import com.swent.assos.ui.components.NewsItem

@Composable
fun ManageAssociation(assoId: String, navigationActions: NavigationActions) {
    val viewModel: AssoViewModel = hiltViewModel()

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
          Text(
              text = association.fullname,
              style = MaterialTheme.typography.headlineLarge,
              modifier = Modifier.padding(top = 16.dp))
          Image(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = null,
              modifier = Modifier
                  .testTag("GoBackButton")
                  .clickable { navigationActions.goBack() })
        }
        item {
          Text(
              text = association.description,
              style = MaterialTheme.typography.bodyMedium,
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp, vertical = 8.dp))
        }
        item {
          Button(
              onClick = { /* TODO */},
              colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("Edit description", color = Color.White)
              }
        }
        item {
          HeaderWithButton(
              header = "Upcoming Events", buttonText = "Add Event", onButtonClick = { navigationActions.navigateTo(Destinations.CREATE_EVENT.route + "/${assoId}") })
        }
        item {
          LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
            items(events) {
              EventItem(it, navigationActions)
              Spacer(modifier = Modifier.width(8.dp))
            }
          }
        }
        item {
          HeaderWithButton(
              header = "Latest Posts", buttonText = "Add Post", onButtonClick = { navigationActions.navigateTo(Destinations.CREATE_NEWS.route + "/${assoId}") })
        }
        item {
          LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
            items(news) {
              NewsItem(it, navigationActions)
              Spacer(modifier = Modifier.width(8.dp))
            }
          }
        }
      }
}

@Composable
fun HeaderWithButton(header: String, buttonText: String, onButtonClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = header, style = MaterialTheme.typography.headlineMedium)
        Button(
            onClick = onButtonClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(buttonText, color = Color.White)
        }
    }
}

private val association =
    Association(
        acronym = "JE",
        fullname = "Junior Entreprise",
        url = "https://junior-entreprise.com/",
        description =
            "Junior Entreprise is a student association that provides consulting services to companies.",
    )
