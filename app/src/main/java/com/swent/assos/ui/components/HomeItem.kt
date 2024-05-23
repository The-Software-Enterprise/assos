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
import com.swent.assos.model.data.News
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.model.view.NewsViewModel
import java.time.LocalDateTime

@Composable
fun HomeItem(id: String, navigationActions: NavigationActions) {

  val viewModel: NewsViewModel = hiltViewModel()
  val allNews by viewModel.allNewsOfAllAssos.collectAsState()

  val eventViewModel: EventViewModel = hiltViewModel()
  val event by eventViewModel.event.collectAsState()

  val title: String
  val description: String
  var assoId: String = ""

  val news =
      allNews.find { it.id == id }
          ?: News(
              id = "0000",
              title = "NO TITLE",
              description = "NO DESCRIPTION",
              images = listOf(Uri.EMPTY),
              createdAt = LocalDateTime.now(),
              associationId = "",
              eventIds = mutableListOf("0000"))

  if (news.eventIds.isNotEmpty()) {
    eventViewModel.getEvent(news.eventIds[0])
    assoId = event.associationId
  }

  title = news.title
  description = news.description

  Box(
      modifier =
          Modifier.background(MaterialTheme.colorScheme.background)
              .fillMaxWidth()
              .height(100.dp)
              .clickable {
                navigationActions.navigateTo(
                    if (news.eventIds.isEmpty()) {
                      Destinations.NEWS_DETAILS.route + "/${news.id}"
                    } else {
                      Destinations.EVENT_DETAILS.route + "/${news.eventIds[0]}" + "/${assoId}"
                    })
              }
              .testTag("NewsListItem")) {
        Row(
            modifier = Modifier.fillMaxSize().testTag("NewsItemRow"),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically) {
              if (news.images.isNotEmpty()) {
                AsyncImage(
                    model = news.images[0],
                    contentDescription = "news image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.width(100.dp).padding(6.dp).clip(RoundedCornerShape(15.dp)),
                )
              } else {
                AsyncImage(
                    model = R.drawable.ic_launcher_foreground,
                    contentDescription = "news image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.width(100.dp).padding(6.dp).clip(RoundedCornerShape(15.dp)))
              }
              Column(
                  modifier =
                      Modifier.padding(start = 16.dp, top = 20.dp)
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
                        modifier = Modifier.weight(1f).testTag("NewsItemsDescription"),
                        text = description,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        color = MaterialTheme.colorScheme.onBackground)
                  }
            }
      }
}
