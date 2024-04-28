package com.swent.assos.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.swent.assos.model.data.Event
import com.swent.assos.model.formatDateTime
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import java.time.LocalDateTime

@Composable
fun EventItem(event: Event, navigationActions: NavigationActions) {
  Surface(
      modifier =
          Modifier.width(200.dp).padding(vertical = 4.dp).clickable {
            navigationActions.navigateTo(Destinations.EVENT_DETAILS.route + "/${event.id}")
          },
      color = Color(0xFFE0E0E0),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
      Text(text = event.title, style = MaterialTheme.typography.titleMedium)
      Spacer(modifier = Modifier.height(8.dp))
      Text(text = event.description)
      Spacer(modifier = Modifier.height(8.dp))
      Text(
          text = formatDateTime(event.startTime ?: LocalDateTime.now()),
          style = MaterialTheme.typography.bodySmall)
      Spacer(modifier = Modifier.height(8.dp))
      Text(
          text = formatDateTime(event.endTime ?: LocalDateTime.now()),
          style = MaterialTheme.typography.bodySmall)
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}
