package com.swent.assos.ui.screens.assoDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.NewsViewModel
import com.swent.assos.ui.theme.Typography

@Composable
fun NewsDetails(newsId: String, navigationActions: NavigationActions) {

  val viewModel: NewsViewModel = hiltViewModel()
  val news by viewModel.news.collectAsState()

  Scaffold(
      modifier =
          Modifier.verticalScroll(rememberScrollState())
              .fillMaxSize()
              .background(Color.White)
              .padding(top = 30.dp),
      topBar = { NewsPageTitle(news.title) }) { paddingValues ->
        // Header Image
        AsyncImage(
            model = news.images,
            contentDescription = "news image",
            contentScale = ContentScale.Fit,
        )

        // Category Label "image 1"
        Text(text = "Image 1", modifier = Modifier.padding(paddingValues))

        // Image Row or Grid
        // Here we are creating a simple row, you might want to use a LazyHorizontalGrid for more
        // complex layouts
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              // Replace this with your actual data source
              items(2) { imageUrl ->
                Card(
                    modifier = Modifier.size(100.dp),
                ) {
                  AsyncImage(
                      model = imageUrl,
                      contentDescription = "news image",
                      contentScale = ContentScale.Fit,
                  )
                }
              }
            }

        // More content...

        // Bottom buttons
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly) {
              Button(onClick = { /* Handle staff action */}) { Text("Staff") }
              Button(onClick = { /* Handle buy ticket action */}) {
                Text("Buy a ticket", color = Color.White)
              }
            }
      }
}

@Composable
fun NewsPageTitle(title: String) {
  Box(
      modifier =
          Modifier.width(360.dp).height(116.dp).background(Color.White).padding(bottom = 24.dp),
  ) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start) {
          Text(
              text = title,
              style =
                  Typography.bodyLarge.copy(
                      fontWeight = FontWeight.Bold, // Adjust the weight as needed
                      fontSize = 30.sp,
                      color = Color(0xFF1D1B20)),
              modifier = Modifier.align(Alignment.CenterHorizontally))
          // Add any other child views if needed
        }
  }
}
