package com.swent.assos.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.swent.assos.model.data.News
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.ui.theme.Typography

@Composable
fun HomeItem(news: News, navigationActions: NavigationActions) {
  Box(modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Color.White).fillMaxWidth()) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()) {
          AsyncImage(
              model = news.imageUrl,
              contentDescription = "news image",
              contentScale = ContentScale.Fit,
              modifier = Modifier.width(114.dp).height(108.dp))
          Column(
              verticalArrangement = Arrangement.spacedBy(10.dp),
              horizontalAlignment = Alignment.Start,
              modifier =
                  Modifier.padding(
                          start =
                              16
                                  .dp) // Assuming there's some spacing between the image and the
                                       // texts
                      .align(Alignment.CenterVertically)) {
                Text(
                    text = news.title,
                    style =
                        Typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold, // Adjust the weight as needed
                            fontSize = 16.sp,
                            color = Color(0xFF1D1B20)),
                    modifier = Modifier.width(173.dp).height(24.dp))
                Text(
                    text = news.description,
                    style =
                        Typography.bodyLarge.copy(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = Color(0xFF49454F),
                            letterSpacing = 0.25.sp),
                    modifier = Modifier.width(173.dp).height(60.dp))
              }
        }
  }
}
