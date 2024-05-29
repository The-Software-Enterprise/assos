package com.swent.assos.ui.screens.manageAsso.createEvent

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.CreateNewsViewModel
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.ui.components.PageTitleWithGoBack
import com.swent.assos.ui.screens.manageAsso.createEvent.components.EventContent
import com.swent.assos.ui.screens.manageAsso.createEvent.components.FloatingButtons

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateEvent(
    assoId: String,
    navigationActions: NavigationActions,
    viewModel: EventViewModel = hiltViewModel(),
) {
  val context = LocalContext.current

  val createNewsViewModel: CreateNewsViewModel = hiltViewModel()

  val news by createNewsViewModel.news.collectAsState()

  val event by viewModel.event.collectAsState()
  val canCreate =
      event.description.isNotEmpty() && event.image != Uri.EMPTY && event.title.isNotEmpty()

  val lazyListState = rememberLazyListState()

  LaunchedEffect(key1 = Unit) {
    event.associationId = assoId
    news.associationId = assoId
  }

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("CreateEventScreen"),
      topBar = {
        PageTitleWithGoBack(title = "Create an event", navigationActions = navigationActions) {
          Box(
              modifier =
                  Modifier.padding(end = 16.dp)
                      .clip(RoundedCornerShape(20))
                      .background(
                          MaterialTheme.colorScheme.primary.copy(
                              alpha = if (canCreate) 1f else 0.5f))
                      .clickable {
                        if (canCreate)
                            viewModel.createEvent(
                                onSuccess = {
                                  navigationActions.goBack()
                                  Toast.makeText(
                                          context,
                                          "The event has been successfully created!",
                                          Toast.LENGTH_SHORT)
                                      .show()
                                },
                                onError = {
                                  Toast.makeText(
                                          context,
                                          "Unfortunately, the event could not be created. Please try again!",
                                          Toast.LENGTH_SHORT)
                                      .show()
                                })
                        createNewsViewModel.setTitle(event.title)
                        createNewsViewModel.setDescription(event.description)
                        createNewsViewModel.addImages(listOf(event.image))
                        createNewsViewModel.setEventId(event.id)
                        createNewsViewModel.createNews(assoId, onSuccess = {}, onError = {})
                      }
                      .padding(vertical = 5.dp, horizontal = 10.dp)
                      .testTag("CreateButton")) {
                Text(
                    text = "Create",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onPrimary)
              }
        }
      },
      floatingActionButton = {
        FloatingButtons(viewModel = viewModel, lazyListState = lazyListState)
      },
  ) { paddingValues ->
    EventContent(
        viewModel = viewModel,
        paddingValues = paddingValues,
        isEdition = true,
        lazyListState = lazyListState,
        eventId = event.id,
        navigationActions = navigationActions)
  }
}
