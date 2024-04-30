package com.swent.assos.ui.screens.assoDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.swent.assos.R
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.User
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.ui.components.EventItem
import com.swent.assos.ui.components.NewsItem

@OptIn(ExperimentalComposeUiApi::class)
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

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("AssoDetailsScreen"),
      topBar = {
        TopAssoBar(
            assoId = assoId,
            asso = association,
            navigationActions = navigationActions,
            currentUser = currentUser,
            viewModel = viewModel)
      }) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues).testTag("Content")) {
          item {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier =
                    Modifier.fillMaxWidth()
                        .padding(10.dp)
                        .height(200.dp)
                        .background(Color.Gray, shape = RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center)

            Text(
                text = association.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 0.dp))

            Text(
                text = "Upcoming Events",
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp))

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
                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                  fontWeight = FontWeight.Medium,
                  modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }
            Text(
                text = "Latest Posts",
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

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
            Spacer(modifier = Modifier.height(20.dp))
          }
        }
      }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAssoBar(
    assoId: String,
    asso: Association,
    navigationActions: NavigationActions,
    currentUser: User,
    viewModel: AssoViewModel
) {
  MediumTopAppBar(
      colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
      modifier = Modifier.testTag("Header"),
      title = { Text(asso.acronym, modifier = Modifier.testTag("Title")) },
      navigationIcon = {
        Image(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier.testTag("GoBackButton").clickable { navigationActions.goBack() })
      },
      actions = {
        AssistChip(
            colors =
                if (currentUser.following.contains(assoId))
                    AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.primary)
                else AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.tertiary),
            border = null,
            modifier = Modifier.testTag("FollowButton").padding(5.dp),
            onClick = {
              if (currentUser.following.contains(assoId)) viewModel.unfollowAssociation(assoId)
              else viewModel.followAssociation(assoId)
            },
            label = {
              if (currentUser.following.contains(assoId)) {
                Text(
                    text = "Following",
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                    fontWeight = FontWeight.Medium,
                )
              } else {
                Text(
                    text = "Follow",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                    fontWeight = FontWeight.Medium,
                )
              }
            },
        )
      })
}

@Composable
fun CustomFAB(onClick: () -> Unit) {
  Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
  ) {
    FloatingActionButton(
        onClick = onClick,
        modifier =
            Modifier.shadow(8.dp, shape = RoundedCornerShape(25), clip = false)
                .background(color = Color(0xFF5465FF), shape = RoundedCornerShape(size = 16.dp))
                .width(92.dp)
                .height(42.dp),
        containerColor = Color(0xFF5465FF),
    ) {
      Text(
          text = "Join us",
          fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
          fontWeight = FontWeight.SemiBold,
          color = Color.White,
          style = MaterialTheme.typography.bodyMedium)
    }
  }
}
