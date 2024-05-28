package com.swent.assos.ui.screens.manageAsso

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.swent.assos.ui.components.EventItem
import com.swent.assos.ui.components.NewsItem
import com.swent.assos.ui.theme.GraySeparator

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ManageAssociation(assoId: String, navigationActions: NavigationActions) {
  val viewModel: AssoViewModel = hiltViewModel()

  val association by viewModel.association.collectAsState()
  val news by viewModel.news.collectAsState()
  val events by viewModel.events.collectAsState()

  val listStateNews = rememberLazyListState()
  val listStateEvents = rememberLazyListState()

  val launcher =
      rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result
        ->
        if (result.resultCode == Activity.RESULT_OK) {
          viewModel.setBanner(result.data?.data)
        }
      }

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
      modifier = Modifier
        .semantics { testTagsAsResourceId = true }
        .testTag("ManageAssoScreen"),
      topBar = { TopAssoBar(asso = association, navigationActions = navigationActions) }) {
          paddingValues ->
        LazyColumn(
            modifier = Modifier
              .padding(paddingValues)
              .testTag("Content"),
            horizontalAlignment = Alignment.CenterHorizontally) {
              item {
                Box {
                  Image(
                      painter =
                          if (association.banner != Uri.EMPTY) {
                            rememberAsyncImagePainter(association.banner)
                          } else {
                            painterResource(id = R.drawable.ic_launcher_foreground)
                          },
                      contentDescription = null,
                      modifier =
                      Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(150.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(GraySeparator),
                      contentScale = ContentScale.Crop,
                      alignment = Alignment.Center)

                  FloatingActionButton(
                      onClick = {
                        val pickImageIntent = Intent(Intent.ACTION_PICK)
                        pickImageIntent.type = "image/*"
                        launcher.launch(pickImageIntent)
                      },
                      modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.TopEnd)
                        .padding(5.dp),
                      containerColor = MaterialTheme.colorScheme.primary,
                  ) {
                    Icon(
                        Icons.Default.ModeEdit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary)
                  }
                }

                HeaderWithButtonWithIcon(
                    header = "Upcoming Events",
                    buttonText = "Add Event",
                    onButtonClick = {
                      navigationActions.navigateTo(Destinations.CREATE_EVENT.route + "/${assoId}")
                    },
                    modifierButton = Modifier.testTag("AddEventButton"))

                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                  items(events) {
                    EventItem(it, navigationActions, association)
                    Spacer(modifier = Modifier.width(8.dp))
                  }
                }
                HeaderWithButtonWithIcon(
                    header = "Latest Posts",
                    buttonText = "Add Post ",
                    onButtonClick = {
                      navigationActions.navigateTo(Destinations.CREATE_NEWS.route + "/${assoId}")
                    },
                    modifierButton = Modifier.testTag("AddPostButton"))
                LazyRow(
                    modifier = Modifier.testTag("NewsItem"),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                      items(news) {
                        NewsItem(it, navigationActions)
                        Spacer(modifier = Modifier.width(8.dp))
                      }
                    }
                Button(
                  modifier =
                  Modifier
                    .testTag("ApplicationsButton")
                    .padding(30.dp)
                    .width(250.dp)
                    .height(60.dp),
                  shape = RoundedCornerShape(16.dp),
                  onClick = {
                    navigationActions.navigateTo(
                      Destinations.APPLICATION_MANAGEMENT.route + "/${assoId}")
                  }) {
                  Icon(imageVector = Icons.Default.Inbox, contentDescription = null)
                  Text("View Applications", modifier = Modifier.padding(start = 12.dp), fontSize = 18.sp)
                }
              }
            }
      }
}

@Composable
fun HeaderWithButtonWithIcon(
    header: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifierButton: Modifier
) {
  Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = header, style = MaterialTheme.typography.headlineMedium, fontSize = 20.sp)
        Button(
            modifier = modifierButton,
            onClick = onButtonClick,
            colors =
                ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                Text(text = buttonText, fontWeight = FontWeight.Medium)
              }
            }
      }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAssoBar(asso: Association, navigationActions: NavigationActions) {
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
            modifier = Modifier
              .testTag("GoBackButton")
              .clickable { navigationActions.goBack() })
      })
}
