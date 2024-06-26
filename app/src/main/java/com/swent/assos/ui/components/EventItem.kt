package com.swent.assos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.shadows_item

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EventItem(event: Event, navigationActions: NavigationActions, asso: Association) {
  Card(
      colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
      shape = RoundedCornerShape(12.dp),
      modifier =
          shadows_item(0.dp, 0.dp, 0.dp, 0.dp, RoundedCornerShape(12.dp))
              .testTag("EventItem")
              .height(150.dp)
              .background(
                  color = MaterialTheme.colorScheme.background,
                  shape = RoundedCornerShape(size = 12.dp))
              .clickable {
                navigationActions.navigateTo(
                    Destinations.EVENT_DETAILS.route + "/${event.id}" + "/${asso.id}")
              }
              .semantics { testTagsAsResourceId = true }
              .testTag("EventItem")) {
        Column(
            modifier = Modifier.width(200.dp), horizontalAlignment = Alignment.CenterHorizontally) {
              Image(
                  painter = rememberAsyncImagePainter(event.image),
                  contentDescription = null,
                  contentScale = ContentScale.Crop,
                  modifier =
                      Modifier.height(75.dp)
                          .background(MaterialTheme.colorScheme.outline)
                          .testTag("EventItemImage"))
              Text(
                  text = event.title,
                  style = MaterialTheme.typography.titleMedium,
                  modifier = Modifier.padding(8.dp),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis)
              Text(
                  text = event.description,
                  style = MaterialTheme.typography.bodyMedium,
                  modifier = Modifier.padding(5.dp),
                  maxLines = 5,
                  overflow = TextOverflow.Ellipsis)
            }
      }
}
