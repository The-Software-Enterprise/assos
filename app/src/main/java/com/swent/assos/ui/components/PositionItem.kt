package com.swent.assos.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.swent.assos.model.data.OpenPositions
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.shadows_item

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PostionItem(pos: OpenPositions, assoId: String, navigationActions: NavigationActions) {
  Card(
      colors = CardDefaults.cardColors(MaterialTheme.colorScheme.outlineVariant),
      shape = RoundedCornerShape(12.dp),
      modifier =
          shadows_item(0.dp, 0.dp, 0.dp, 0.dp, RoundedCornerShape(12.dp))
              .semantics { testTagsAsResourceId = true }
              .testTag("PosItem")) {
        Column(
            modifier =
                Modifier.width(200.dp).clickable {
                  navigationActions.navigateTo(
                      Destinations.POS_DETAILS.route + "/${assoId}" + "/${pos.id}")
                },
            horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                  text = pos.title,
                  style = MaterialTheme.typography.titleMedium,
                  modifier = Modifier.padding(8.dp).testTag("NewsItemTitle"),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis)
            }
      }
}
