package com.swent.assos.ui.screens.manageAsso

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.data.Association
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.ui.components.EventItem
import com.swent.assos.ui.components.NewsItem

@OptIn(ExperimentalComposeUiApi::class)
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

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("ManageAssoScreen"),
      topBar = { TopAssoBar(asso = association, navigationActions = navigationActions) }) {
          paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).testTag("Content"),
            horizontalAlignment = Alignment.CenterHorizontally) {
              item {
                Text(
                    text = association.fullname,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier =
                        Modifier.testTag("DescriptionField")
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp))
                Button(
                    modifier = Modifier.testTag("EditDescriptionButton"),
                    onClick = { /* TODO */},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                      Text("Edit description", color = Color.White)
                    }
                HeaderWithButton(
                    header = "Upcoming Events",
                    buttonText = "Add Event",
                    onButtonClick = {
                      navigationActions.navigateTo(Destinations.CREATE_EVENT.route + "/${assoId}")
                    },
                    modifierButton = Modifier.testTag("AddEventButton"))

                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                  items(events) {
                    EventItem(it, navigationActions)
                    Spacer(modifier = Modifier.width(8.dp))
                  }
                }
                HeaderWithButton(
                    header = "Latest Posts",
                    buttonText = "Add Post",
                    onButtonClick = {
                      navigationActions.navigateTo(Destinations.CREATE_NEWS.route + "/${assoId}")
                    },
                    modifierButton = Modifier.testTag("AddPostButton"))
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                  items(news) {
                    NewsItem(it, navigationActions)
                    Spacer(modifier = Modifier.width(8.dp))
                  }
                }

                Spacer(modifier = Modifier.height(20.dp))
              }
            }
      }
}

@Composable
fun HeaderWithButton(
    header: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifierButton: Modifier
) {
  Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = header, style = MaterialTheme.typography.headlineMedium)
        Button(
            modifier = modifierButton,
            onClick = onButtonClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
              Text(buttonText, color = Color.White)
            }
      }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAssoBar(asso: Association, navigationActions: NavigationActions) {
  MediumTopAppBar(
      modifier = Modifier.testTag("Header"),
      title = { Text(asso.acronym, modifier = Modifier.testTag("Title")) },
      navigationIcon = {
        Image(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier.testTag("GoBackButton").clickable { navigationActions.goBack() })
      })
}
