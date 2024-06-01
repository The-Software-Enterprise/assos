package com.swent.assos.ui.screens.assoDetails

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.R
import com.swent.assos.model.data.Association
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.model.view.ProfileViewModel
import com.swent.assos.ui.components.EventItem
import com.swent.assos.ui.components.NewsItem
import com.swent.assos.ui.components.PostionItem

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AssoDetails(assoId: String, navigationActions: NavigationActions) {
  val viewModel: AssoViewModel = hiltViewModel()

  val currentUser by viewModel.currentUser.collectAsState()
  val association by viewModel.association.collectAsState()
  val news by viewModel.news.collectAsState()
  val events by viewModel.events.collectAsState()

  val pos by viewModel.positions.collectAsState()

  val listStateNews = rememberLazyListState()
  val listStateEvents = rememberLazyListState()

  val listStatePos = rememberLazyListState()

  val context = LocalContext.current

  LaunchedEffect(key1 = Unit) {
    viewModel.getAssociation(assoId)
    viewModel.getNews(assoId)
    viewModel.getEvents(assoId)
    viewModel.getPositions(assoId)
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
        TopAssoBar(asso = association, navigationActions = navigationActions, viewModel = viewModel)
      },
      floatingActionButton = {
        if (!currentUser.associations.map { it.first }.contains(assoId)) {

          val applied = viewModel.applied.collectAsState()

          AssistChip(
              colors =
                  if (applied.value)
                      AssistChipDefaults.assistChipColors(
                          containerColor = MaterialTheme.colorScheme.surfaceVariant)
                  else
                      AssistChipDefaults.assistChipColors(
                          containerColor = MaterialTheme.colorScheme.secondary),
              border = null,
              modifier = Modifier.testTag("JoinUsButton").padding(5.dp),
              onClick = {
                if (applied.value) {
                  viewModel.removeRequestToJoin(
                      currentUser.id,
                      assoId,
                      Toast.makeText(
                              context,
                              "You have successfully removed your request to join the association",
                              Toast.LENGTH_SHORT)
                          .show())
                } else {
                  viewModel.applyToAssociation(
                      currentUser.id,
                      Toast.makeText(
                              context,
                              "You have successfully applied to join the association",
                              Toast.LENGTH_SHORT)
                          .show())
                }
              },
              label = {
                if (applied.value) {
                  Text(
                      text = "Remove application",
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight.Medium,
                  )
                } else {
                  Text(
                      text = "Join Us",
                      color = MaterialTheme.colorScheme.onSecondary,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight.Medium,
                  )
                }
              },
          )
        }
      },
      floatingActionButtonPosition = FabPosition.Center,
  ) { paddingValues ->
    LazyColumn(modifier = Modifier.padding(paddingValues).testTag("Content")) {
      item {
        Image(
            painter =
                if (association.banner != Uri.EMPTY) {
                  rememberAsyncImagePainter(association.banner)
                } else {
                  painterResource(id = R.drawable.ic_launcher_foreground)
                },
            contentDescription = null,
            modifier =
                Modifier.fillMaxWidth()
                    .padding(10.dp)
                    .height(150.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(Color.Gray),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center)
      }

      item {
        Text(
            text = association.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 0.dp))
      }

      item {
        Text(
            text = "Upcoming Events",
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp))

        if (events.isNotEmpty()) {
          LazyRow(
              modifier = Modifier.testTag("EventItem"),
              state = listStateEvents,
              contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                items(events) {
                  EventItem(it, navigationActions, association)
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
      }

      item {
        Text(
            text = "Latest Posts",
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

        if (news.isNotEmpty()) {
          LazyRow(
              modifier = Modifier.testTag("Newsitem"),
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

      item {
        Text(
            text = "Latest Positions",
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

        if (pos.isNotEmpty()) {
          LazyRow(
              modifier = Modifier.testTag("Positionitem"),
              contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                items(pos) {
                  PostionItem(it, assoId, navigationActions)
                  Spacer(modifier = Modifier.width(8.dp))
                }
              }
        } else {
          Text(
              text = "No latest positions",
              style = MaterialTheme.typography.bodyMedium,
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        }
      }

      item {
        Text(
            text = "The Committee",
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
            fontWeight = FontWeight.Bold,
            modifier =
                Modifier.padding(horizontal = 16.dp, vertical = 10.dp).clickable {
                  navigationActions.navigateTo(
                      Destinations.COMMITTEE_DETAILS.route +
                          "/${assoId}" +
                          "/${association.acronym}")
                })
        Spacer(modifier = Modifier.height(50.dp))
      }
      item { Spacer(modifier = Modifier.height(20.dp)) }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAssoBar(asso: Association, navigationActions: NavigationActions, viewModel: AssoViewModel) {
  val associationFollowed = viewModel.associationFollowed.collectAsState()
  val profileViewModel: ProfileViewModel = hiltViewModel()

  MediumTopAppBar(
      colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
      modifier = Modifier.testTag("Header"),
      title = {
        Text(
            asso.acronym,
            modifier = Modifier.testTag("Title"),
            style =
                TextStyle(
                    fontSize = 30.sp,
                    lineHeight = 32.sp,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground))
      },
      navigationIcon = {
        Image(
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier.testTag("GoBackButton").clickable { navigationActions.goBack() })
      },
      actions = {
        if (asso.id != "") {
          AssistChip(
              colors =
                  if (associationFollowed.value)
                      AssistChipDefaults.assistChipColors(
                          containerColor = MaterialTheme.colorScheme.surfaceVariant)
                  else
                      AssistChipDefaults.assistChipColors(
                          containerColor = MaterialTheme.colorScheme.secondary),
              border = null,
              modifier = Modifier.testTag("FollowButton").padding(5.dp),
              onClick = {
                if (associationFollowed.value) {
                  viewModel.unfollowAssociation(asso.id)
                } else {
                  viewModel.followAssociation(asso.id)
                }
                profileViewModel.updateNeeded()
              },
              label = {
                if (associationFollowed.value) {
                  Text(
                      text = "Following",
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight.Medium,
                  )
                } else {
                  Text(
                      text = "Follow",
                      color = MaterialTheme.colorScheme.onSecondary,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight.Medium,
                  )
                }
              },
          )
        }
      })
}

@Composable
fun JoinUsButton(onClick: () -> Unit, text: String = "Join us") {
  FloatingActionButton(
      onClick = onClick,
      modifier = Modifier.height(42.dp).testTag("JoinButton"),
      elevation = FloatingActionButtonDefaults.elevation(5.dp),
      containerColor = MaterialTheme.colorScheme.primary,
  ) {
    Text(
        modifier = Modifier.padding(horizontal = 10.dp),
        text = text,
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.bodyMedium)
  }
}
