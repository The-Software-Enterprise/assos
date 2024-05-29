package com.swent.assos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.swent.assos.R
import com.swent.assos.model.data.Event
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.EventViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EventsItemSaved(events: Event, navigationActions: NavigationActions) {

  val viewModel: EventViewModel = hiltViewModel()
  // val allNews by viewModel.allNewsOfAllAssos.collectAsState()

  val eventViewModel: EventViewModel = hiltViewModel()
  // val event by eventViewModel.event.collectAsState()

  val title: String
  val description: String
  var assoId: String = ""

  /*if (news.eventId != "") {
      eventViewModel.getEvent(news.eventId)
      assoId = event.associationId
  }*/

  title = events.title
  description = events.description

  Box(
      modifier =
          Modifier.background(MaterialTheme.colorScheme.background)
              .fillMaxWidth()
              .height(100.dp)
              .clickable {
                navigationActions.navigateTo(
                    if (events.id == "") {
                      Destinations.NEWS_DETAILS.route + "/${events.id}"
                    } else {
                      Destinations.EVENT_DETAILS.route +
                          "/${events.id}" +
                          "/${events.associationId}"
                    })
              }
              .testTag("NewsListItem")) {
        Row(
            modifier = Modifier.fillMaxSize().testTag("NewsItemRow"),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically) {
              if (events.image != null) {
                AsyncImage(
                    model = events.image,
                    contentDescription = "event image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.width(100.dp).padding(6.dp).clip(RoundedCornerShape(15.dp)),
                )
              } else {
                AsyncImage(
                    model = R.drawable.ic_launcher_foreground,
                    contentDescription = "event image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.width(100.dp).padding(6.dp).clip(RoundedCornerShape(15.dp)))
              }
              Column(
                  modifier =
                      Modifier.padding(start = 16.dp, top = 20.dp)
                          .align(Alignment.CenterVertically)
                          .testTag("EventsItemColumn"),
                  verticalArrangement = Arrangement.spacedBy(5.dp),
                  horizontalAlignment = Alignment.Start) {
                    Text(
                        modifier = Modifier.testTag("EventsItemsTitle"),
                        text = title,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground)
                    Text(
                        modifier = Modifier.weight(1f).testTag("EventsItemsDescription"),
                        text = description,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        color = MaterialTheme.colorScheme.onBackground)
                  }
            }
      }
}
