package com.swent.assos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.data.Association
import com.swent.assos.model.view.NewsViewModel
import com.swent.assos.ui.theme.Purple40
import com.swent.assos.ui.theme.Purple80

@Preview
@Composable
fun News() {
  val viewModel: NewsViewModel = hiltViewModel()
  val news by viewModel.news.collectAsState()
  val listState = rememberLazyListState()

  LaunchedEffect(listState) {
    snapshotFlow { listState.layoutInfo.visibleItemsInfo }
        .collect { visibleItems ->
          if (visibleItems.isNotEmpty() && visibleItems.last().index == news.size - 1) {
            viewModel.loadMoreAssociations()
          }
        }
  }

  Scaffold(
      modifier = Modifier.testTag("NewsScreen"),
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
              modifier =
                  Modifier.padding(8.dp)
                      .align(Alignment.CenterHorizontally)
                      .wrapContentHeight(align = Alignment.CenterVertically)) {
                Text(
                    modifier = Modifier.testTag("AppTitle_1"),
                    text = "Student",
                    style =
                        TextStyle(
                            fontSize = 35.sp,
                            fontFamily = FontFamily(Font(R.font.impact)),
                            fontWeight = FontWeight(400),
                            color = Purple80))
                Text(
                    modifier = Modifier.testTag("AppTitle_2"),
                    text = "Sphere",
                    style =
                        TextStyle(
                            fontSize = 35.sp,
                            fontFamily = FontFamily(Font(R.font.impact)),
                            fontWeight = FontWeight(400),
                            color = Purple40))
              }
        }
      }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).testTag("NewsList"),
            userScrollEnabled = true,
            state = listState) {
              items(news) {
                Box(
                    modifier =
                        Modifier.padding(16.dp)
                            .shadow(
                                elevation = 10.dp,
                                spotColor = Color.Gray,
                                ambientColor = Color.Gray)
                            .fillMaxSize()
                            .background(
                                color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 15.dp))
                            .testTag("NewsListItem")) {
                      Column(modifier = Modifier.padding(16.dp)) {
                        var association by remember {
                          mutableStateOf(
                              Association(
                                  id = "",
                                  acronym = "",
                                  fullname = "",
                                  description = "",
                                  logo = ""))
                        }
                        viewModel.getNewsAssociation(it.associationId) { association = it }
                        Text(
                            modifier = Modifier.testTag("ItemsTitle"),
                            fontSize = 20.sp,
                            text = it.title,
                        )
                        Text(
                            modifier = Modifier.testTag("ItemsDescription"),
                            text = it.description,
                        )
                        Text(
                            modifier = Modifier.testTag("ItemsDate"),
                            text = it.createdAt.toString(),
                        )
                        Text(
                            modifier = Modifier.testTag("ItemsAssociation"),
                            text = association.fullname,
                        )
                        it.eventIds.forEach {
                          Text(
                              text = it,
                          )
                        }
                      }
                    }
              }
            }
      }
}
