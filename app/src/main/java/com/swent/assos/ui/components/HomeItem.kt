package com.swent.assos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swent.assos.R
import com.swent.assos.model.data.News
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions

@Composable
fun HomeItem(news: News, navigationActions: NavigationActions) {
  Box(
      modifier =
          Modifier
              //.shadow(2.dp, RoundedCornerShape(2.dp))
              .background(
                  color = MaterialTheme.colorScheme.surface,
                  shape = RoundedCornerShape(size = 15.dp))
              .fillMaxWidth()
              .height(100.dp)
              .clickable {
                navigationActions.navigateTo(Destinations.NEWS_DETAILS.route + "/${news.id}")
              }
              .testTag("NewsListItem")) {
        Row(
            modifier = Modifier.fillMaxSize().border(width = 0.5.dp, color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(15.dp)).background(color = Color.Transparent, shape = RoundedCornerShape(15.dp)).testTag("NewsItemRow"),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically) {
              if (news.images.isNotEmpty()) {
                AsyncImage(
                    model = news.images[0],
                    contentDescription = "news image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.width(80.dp).fillMaxWidth())
              } else {
                AsyncImage(
                    model = R.drawable.ic_launcher_foreground,
                    contentDescription = "news image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.width(80.dp).fillMaxWidth())
              }
              Column(
                  modifier =
                      Modifier.padding(start = 16.dp)
                          .align(Alignment.CenterVertically)
                          .testTag("NewsItemColumn"),
                  verticalArrangement = Arrangement.spacedBy(10.dp),
                  horizontalAlignment = Alignment.Start) {
                    Text(
                        modifier = Modifier.width(173.dp).testTag("NewsItemsTitle"),
                        text = news.title,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground)
                    Text(
                        modifier =
                            Modifier.width(173.dp).weight(1f).testTag("NewsItemsDescription"),
                        text = news.description,
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onBackground)
                  }
            }
      }
}
