package com.swent.assos.ui.components

import android.net.Uri
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.swent.assos.model.data.News
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.timestampToLocalDateTime
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.model.view.NewsViewModel
import java.time.LocalDateTime

@Composable
fun HomeItem(id: String, isNews: Boolean, navigationActions: NavigationActions) {

  val eventViewModel: EventViewModel = hiltViewModel()
  val events by eventViewModel.events.collectAsState()

  val viewModel: NewsViewModel = hiltViewModel()
  val allNews by viewModel.allNewsOfAllAssos.collectAsState()

  var title: String = "title"
  var description: String = "description"

  var news =
      News(
          id = "0000",
          title = "NO TITLE",
          description = "NO DESCRIPTION",
          images = listOf(Uri.EMPTY),
          createdAt = LocalDateTime.now(),
          associationId = "",
          eventIds = mutableListOf())

    var event = Event(
        id = "0000",
        title = "title",
        description = "description",
        associationId = "associationId")


  if (isNews) {
    LaunchedEffect(key1 = Unit) { viewModel.loadNews() }

    news =
        allNews.find { it.id == id }
            ?: News(
                id = "0000",
                title = "NO TITLE",
                description = "NO DESCRIPTION",
                images = listOf(Uri.EMPTY),
                createdAt = LocalDateTime.now(),
                associationId = "",
                eventIds = mutableListOf())
    title = news.title
    description = news.description
  } else {

    LaunchedEffect(key1 = Unit) { eventViewModel.getEvent(id) }

      event = events.find { it.id == id } ?: Event(
          id = "0000",
          title = "title",
          description = "description",
          associationId = "associationId")

    title = event.title
    description = event.description
  }

  Box(
      modifier =
      Modifier
          .background(MaterialTheme.colorScheme.background)
          .fillMaxWidth()
          .height(100.dp)
          .clickable {
              navigationActions.navigateTo(
                  if (isNews) {
                      Destinations.NEWS_DETAILS.route + "/${news.id}"
                  } else {
                      Destinations.EVENT_DETAILS.route + "/${event.id}" + "/${event.associationId}"
                  }
              )
          }
          .testTag("NewsListItem")) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .testTag("NewsItemRow"),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically) {
              if (isNews && news.images.isNotEmpty()) {
                AsyncImage(
                    model = news.images[0],
                    contentDescription = "news image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(15.dp)),
                )
              } else if (!isNews && event.image != Uri.EMPTY) {
                AsyncImage(
                    model = event.image,
                    contentDescription = "news image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(15.dp)),
                )
              } else {
                AsyncImage(
                    model = R.drawable.ic_launcher_foreground,
                    contentDescription = "news image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(15.dp)))
              }
              Column(
                  modifier =
                  Modifier
                      .padding(start = 16.dp, top = 20.dp)
                      .align(Alignment.CenterVertically)
                      .testTag("NewsItemColumn"),
                  verticalArrangement = Arrangement.spacedBy(5.dp),
                  horizontalAlignment = Alignment.Start) {
                    Text(
                        modifier = Modifier.testTag("NewsItemsTitle"),
                        text = title,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground)
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("NewsItemsDescription"),
                        text = description,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        color = MaterialTheme.colorScheme.onBackground)
                  }
            }
      }
}
