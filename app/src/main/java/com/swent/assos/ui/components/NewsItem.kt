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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.swent.assos.R
import com.swent.assos.model.data.News
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions

@Composable
fun NewsItem(news: News, navigationActions: NavigationActions) {
  /*Surface(
      modifier =
          Modifier.testTag("NewsItem").width(200.dp).padding(vertical = 4.dp).clickable {
            navigationActions.navigateTo(Destinations.NEWS_DETAILS.route + "/${news.id}")
          },
      //color = Color(0xFFE0E0E0),
      color = Color.White,

  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(50.dp).background(Color.White),
            contentScale = ContentScale.Crop)


      Text(text = news.title, style = MaterialTheme.typography.titleMedium)
      Spacer(modifier = Modifier.height(8.dp))
      Text(text = news.description)
      Spacer(modifier = Modifier.height(8.dp))
    }
  }*/
  Card(
      colors = CardDefaults.cardColors(Color.White),
      shape = RoundedCornerShape(12.dp),
      modifier =
          Modifier.padding(0.dp)
              .border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(12.dp))) {
        Column(
            // modifier = Modifier.fillMaxWidth()
            modifier =
                Modifier.testTag("NewsItem").width(200.dp).padding(vertical = 0.dp).clickable {
                  navigationActions.navigateTo(Destinations.NEWS_DETAILS.route + "/${news.id}")
                },
        ) {
          Image(
              painter = painterResource(id = R.drawable.ic_launcher_foreground),
              contentDescription = null,
              contentScale = ContentScale.FillHeight,
              modifier = Modifier.fillMaxWidth().height(84.dp).background(Color.Gray))

          Spacer(modifier = Modifier.height(8.dp))
          Text(
              text = news.title,
              style = MaterialTheme.typography.titleMedium,
              modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center))
          Spacer(modifier = Modifier.height(8.dp))
          Text(
              text = news.description,
              style = MaterialTheme.typography.bodyMedium,
              modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center))
          Spacer(modifier = Modifier.height(5.dp))
        }
      }
}
