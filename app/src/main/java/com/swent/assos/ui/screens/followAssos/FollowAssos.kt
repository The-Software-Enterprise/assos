package com.swent.assos.ui.screens.followAssos

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.model.view.NewsViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date

@Composable
fun FollowAssociation(association: Association, navigationActions: NavigationActions) {
    val assoViewModel: AssoViewModel = hiltViewModel()
    //TODO : Find a way to have only the associationId and not the whole association as parameter

    val eventsViewModel: EventViewModel = hiltViewModel()
    //val events by eventsViewModel.allEvents.collectAsState(emptyList())

    val newsViewModel: NewsViewModel = hiltViewModel()
    //val news by newsViewModel.allNews.collectAsState(emptyList())


  LazyColumn(
      modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        item {
          HeaderWithButton(
              header = association.fullname, buttonText = "Follow", onButtonClick = { assoViewModel.followAssociation(association.id) })
            Image(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier =
                Modifier.testTag("GoBackButton").clickable { navigationActions.goBack() })
        }

        item {
          Text(
              text = association.description,
              style = MaterialTheme.typography.bodyMedium,
              modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp))
        }

        item {
          Text(
              text = "Upcoming Events",
              style = MaterialTheme.typography.headlineMedium,
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        }

        item {
          LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
            items(events.size) { index ->
              val event = events[index]
              EventBlock(event)
              Spacer(modifier = Modifier.width(8.dp))
            }
          }
        }

        item {
          Text(
              text = "Latest Posts",
              style = MaterialTheme.typography.headlineMedium,
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        }

        item {
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
fun HeaderWithButton(header: String, buttonText: String, onButtonClick: () -> Unit) {
  Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = header, style = MaterialTheme.typography.headlineMedium)
        Button(
            onClick = onButtonClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
              Text(buttonText, color = Color.White)
            }
      }
}

@Composable
fun EventBlock(event: Event) {
  Surface(
      modifier = Modifier.width(200.dp).padding(vertical = 4.dp),
      color = Color(0xFFE0E0E0),
      // elevation = 4.dp
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
      // elevation = 4.dp
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
      Text(text = news.title, style = MaterialTheme.typography.titleMedium)
      Spacer(modifier = Modifier.height(8.dp))
      Text(text = news.description)
      Spacer(modifier = Modifier.height(8.dp))
      // Text(text = formatDateTime(news.date.toString()), style =
      // MaterialTheme.typography.bodySmall)
      // Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

private fun formatDateTime(dateString: String): String {
  val formatter = DateTimeFormatter.ISO_DATE_TIME
  val dateTime = LocalDateTime.parse(dateString, formatter)
  return dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
}

private val association =
    Association(
        acronym = "JE",
        fullname = "Junior Entreprise",
        url = "https://junior-entreprise.com/",
        description =
            "Junior Entreprise is a student association that provides consulting services to companies.",
    )

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
