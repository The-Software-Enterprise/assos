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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions

@Composable
fun EventItem(event: Event, navigationActions: NavigationActions, asso: Association) {
  Card(
      colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
      shape = RoundedCornerShape(12.dp),
      modifier =
          Modifier.shadow(elevation = 6.dp, shape = RoundedCornerShape(12.dp))
              .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
              .clickable {
                navigationActions.navigateTo(
                    Destinations.EVENT_DETAILS.route + "/${event.id}" + "/${asso.id}")
              }) {
        Column(
            modifier = Modifier.width(200.dp), horizontalAlignment = Alignment.CenterHorizontally) {
              Image(
                  painter = rememberAsyncImagePainter(event.image),
                  contentDescription = null,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.height(100.dp).background(MaterialTheme.colorScheme.outline))
              Text(
                  text = event.title,
                  style = MaterialTheme.typography.titleMedium,
                  modifier = Modifier.padding(8.dp))
              Text(
                  text = event.description,
                  style = MaterialTheme.typography.bodyMedium,
                  modifier = Modifier.padding(5.dp))
            }
      }
}
