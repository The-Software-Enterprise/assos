package com.swent.assos.ui.screens.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.ui.components.TopAssoBar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FollowAssociation(asso: Association, navigationActions: NavigationActions) {
  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("AssoDigestScreen"),
      topBar = { TopAssoBar(association = asso, navigationActions = navigationActions) }) {
          paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                  text = asso.description,
                  style = MaterialTheme.typography.bodyMedium,
                  modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp))

              Text(
                  text = "Upcoming Events",
                  style = MaterialTheme.typography.headlineMedium,
                  modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

              LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                items(events.size) { index ->
                  val event = events[index]
                  EventBlock(event)
                  Spacer(modifier = Modifier.width(8.dp))
                }
              }

              Text(
                  text = "Latest Posts",
                  style = MaterialTheme.typography.headlineMedium,
                  modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

              LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                items(news.size) { index ->
                  val n = news[index]
                  NewsBlock(n)
                  Spacer(modifier = Modifier.width(8.dp))
                }
              }
            }
      }
}

@Composable
fun EventBlock(event: Event) {
  Surface(
      modifier = Modifier.width(200.dp).padding(vertical = 4.dp),
      color = Color(0xFFE0E0E0),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
      Text(text = event.title, style = MaterialTheme.typography.titleMedium)
      Spacer(modifier = Modifier.height(8.dp))
      Text(text = event.description)
      Spacer(modifier = Modifier.height(8.dp))
      Text(text = formatDateTime(event.date), style = MaterialTheme.typography.bodySmall)
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

@Composable
fun NewsBlock(news: News) {
  Surface(
      modifier = Modifier.width(200.dp).padding(vertical = 4.dp),
      color = Color(0xFFE0E0E0),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
      Text(text = news.title, style = MaterialTheme.typography.titleMedium)
      Spacer(modifier = Modifier.height(8.dp))
      Text(text = news.description)
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

private fun formatDateTime(dateString: String): String {
  val formatter = DateTimeFormatter.ISO_DATE_TIME
  val dateTime = LocalDateTime.parse(dateString, formatter)
  return dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
}

private val events =
    listOf(
        Event(
            title = "Crepes at Esplanade #1",
            association = association,
            image = "https://source.unsplash.com/random/400x400?sig=1",
            description = "Come and enjoy some delicious crepes at the Esplanade!",
            date = "2024-04-20T12:00:00",
        ),
        Event(
            title = "Crepes at Esplanade #2",
            association = association,
            image = "https://source.unsplash.com/random/400x400?sig=2",
            description = "Come and enjoy some delicious crepes at the Esplanade!",
            date = "2024-05-10T12:00:00",
        ),
        Event(
            title = "Crepes at Esplanade #3",
            association = association,
            image = "https://source.unsplash.com/random/400x400?sig=1",
            description = "Come and enjoy some delicious crepes at the Esplanade!",
            date = "2024-06-10T12:00:00",
        ),
        Event(
            title = "Crepes at Esplanade #4",
            association = association,
            image = "https://source.unsplash.com/random/400x400?sig=2",
            description = "Come and enjoy some delicious crepes at the Esplanade!",
            date = "2024-06-20T12:00:00",
        ),
    )

private val news =
    listOf(
        News(
            title = "Crepes at Esplanade #1",
            associationId = "JE",
            image = "https://source.unsplash.com/random/400x400?sig=1",
            description = "Come and enjoy some delicious crepes at the Esplanade!",
            date = Date(),
            eventId = "1"),
        News(
            title = "Crepes at Esplanade #2",
            associationId = "JE",
            image = "https://source.unsplash.com/random/400x400?sig=1",
            description = "Come and enjoy some delicious crepes at the Esplanade!",
            date = Date(),
            eventId = "2"),
        News(
            title = "Crepes at Esplanade #3",
            associationId = "JE",
            image = "https://source.unsplash.com/random/400x400?sig=1",
            description = "Come and enjoy some delicious crepes at the Esplanade!",
            date = Date(),
            eventId = "2"),
        News(
            title = "Crepes at Esplanade #4",
            associationId = "JE",
            image = "https://source.unsplash.com/random/400x400?sig=1",
            description = "Come and enjoy some delicious crepes at the Esplanade!",
            date = Date(),
            eventId = "4"))
