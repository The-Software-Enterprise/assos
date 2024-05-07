package com.swent.assos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.model.data.Ticket
import com.swent.assos.model.navigation.NavigationActions
import java.time.LocalDateTime

@Composable
fun TicketItem(ticket: Ticket, navigationActions: NavigationActions) {
  Card(
      colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
      shape = RoundedCornerShape(12.dp),
      modifier =
          Modifier.testTag("TicketItem")
              .padding(16.dp)
              .border(
                  width = 0.5.dp,
                  color = MaterialTheme.colorScheme.outline,
                  shape = RoundedCornerShape(12.dp))) {
        Column(
            modifier =
                Modifier.fillMaxWidth().padding(vertical = 0.dp).clickable(false) {
                  // navigationActions.navigateTo(Destinations.NEWS_DETAILS.route + "/${event.id}")
                },
        ) {
          Image(
              painter = rememberAsyncImagePainter(ticket.banner),
              contentDescription = null,
              contentScale = ContentScale.Crop,
              modifier =
                  Modifier.fillMaxWidth()
                      .height(84.dp)
                      .background(MaterialTheme.colorScheme.outline))

          Spacer(modifier = Modifier.height(10.dp))
          Text(
              text = ticket.name,
              style = MaterialTheme.typography.titleMedium,
              modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
          Spacer(modifier = Modifier.height(6.dp))

          Text(
              text = ticket.startTime?.let { dateToReadableString(it) } ?: "",
              style = MaterialTheme.typography.bodyMedium,
              modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
          Spacer(modifier = Modifier.height(10.dp))
        }
      }
}

fun dateToReadableString(date: LocalDateTime): String {
  return "${date.dayOfMonth} ${date.month} ${date.year}, ${date.hour}:${date.minute}"
}
