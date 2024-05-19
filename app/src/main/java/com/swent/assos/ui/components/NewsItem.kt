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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.swent.assos.R
import com.swent.assos.model.data.News
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewsItem(news: News, navigationActions: NavigationActions) {
  Card(
      colors = CardDefaults.cardColors(MaterialTheme.colorScheme.outlineVariant),
      shape = RoundedCornerShape(12.dp),
      modifier =
          Modifier.semantics { testTagsAsResourceId = true }
              .testTag("NewsItem")
              .padding(0.dp)
              .border(
                  width = 0.5.dp,
                  color = MaterialTheme.colorScheme.surfaceVariant,
                  shape = RoundedCornerShape(12.dp))) {
        Column(
            modifier =
                Modifier.width(200.dp).clickable {
                  navigationActions.navigateTo(Destinations.NEWS_DETAILS.route + "/${news.id}")
                },
        ) {
          if (news.images.isNotEmpty()) {
            AsyncImage(
                model = news.images[0],
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier =
                    Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .testTag("NewsItemImage"))
          } else {
            AsyncImage(
                model = R.drawable.ic_launcher_foreground,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier =
                    Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .testTag("NewsItemImage"))
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
              text = news.title,
              style = MaterialTheme.typography.titleMedium,
              modifier =
                  Modifier.fillMaxWidth()
                      .wrapContentSize(Alignment.Center)
                      .testTag("NewsItemTitle"))
          Spacer(modifier = Modifier.height(8.dp))
          Text(
              text = news.description,
              style = MaterialTheme.typography.bodyMedium,
              modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center))
          Spacer(modifier = Modifier.height(5.dp))
        }
      }
}
