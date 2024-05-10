@file:OptIn(ExperimentalMaterial3Api::class)

package com.swent.assos.ui.screens.ticket

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.R
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.ui.components.PageTitleWithGoBack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TicketDetails(eventId: String, navigationActions: NavigationActions) {

  val viewModelEvent: EventViewModel = hiltViewModel()
  val event by viewModelEvent.event.collectAsState()

  LaunchedEffect(key1 = Unit) { viewModelEvent.getEvent(eventId) }

  Scaffold(
      modifier = Modifier
          .semantics { testTagsAsResourceId = true }
          .testTag("TicketDetailsScreen"),
      topBar = { PageTitleWithGoBack(event.title, navigationActions) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .testTag("TicketDetails"),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Image(
              painter = rememberAsyncImagePainter(event.image),
              contentDescription = null,
              contentScale = ContentScale.Crop,
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(horizontal = 16.dp)
                      .height(150.dp)
                      .clip(shape = RoundedCornerShape(20.dp))
                      .background(MaterialTheme.colorScheme.surfaceVariant)
                      .testTag("EventImage"))

          Spacer(modifier = Modifier.height(8.dp))

          Text(
              text = "Start Time: " + event.startTime?.let { dateToReadableString(it) },
              fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
              modifier = Modifier
                  .padding(8.dp)
                  .testTag("StartTime"))
          Text(
              text = "End Time: " + event.endTime?.let { dateToReadableString(it) },
              fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
              modifier = Modifier
                  .padding(8.dp)
                  .testTag("EndTime"))
          Text(
              text = "Description: ${event.description}",
              fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
              modifier = Modifier
                  .padding(8.dp)
                  .testTag("Description"))
        }
      }
}
