package com.swent.assos.ui.components

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
import coil.compose.AsyncImage
import com.swent.assos.R
import com.swent.assos.model.data.News
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.shadows_item

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewsItem(news: News, navigationActions: NavigationActions) {
  Card(
      colors = CardDefaults.cardColors(MaterialTheme.colorScheme.outlineVariant),
      shape = RoundedCornerShape(12.dp),
      modifier =shadows_item(0.dp, 0.dp, 0.dp, 0.dp, RoundedCornerShape(12.dp)).semantics { testTagsAsResourceId = true }
              .testTag("NewsItem")
              .height(150.dp)) {
        Column(
            modifier =
                Modifier.width(200.dp).clickable {
                  navigationActions.navigateTo(Destinations.NEWS_DETAILS.route + "/${news.id}")
                },
            horizontalAlignment = Alignment.CenterHorizontally) {
              if (news.images.isNotEmpty()) {
                AsyncImage(
                    model = news.images[0],
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier =
                        Modifier.height(75.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .testTag("NewsItemImage"))
              } else {
                AsyncImage(
                    model = R.drawable.ic_launcher_foreground,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier =
                        Modifier.height(75.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .testTag("NewsItemImage"))
              }
              Text(
                  text = news.title,
                  style = MaterialTheme.typography.titleMedium,
                  modifier = Modifier.padding(8.dp).testTag("NewsItemTitle"),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis)
              Text(
                  text = news.description,
                  style = MaterialTheme.typography.bodyMedium,
                  modifier = Modifier.padding(5.dp),
                  maxLines = 5,
                  overflow = TextOverflow.Ellipsis)
            }
      }
}
