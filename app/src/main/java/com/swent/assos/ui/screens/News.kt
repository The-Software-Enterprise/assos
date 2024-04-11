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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
  val newsViewModel: NewsViewModel = hiltViewModel()
  val news by newsViewModel.allNews.collectAsState()
  Scaffold(
      topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
              modifier =
                  Modifier.padding(8.dp)
                      .align(Alignment.CenterHorizontally)
                      .wrapContentHeight(align = Alignment.CenterVertically)) {
                Text(
                    text = "Student",
                    style =
                        TextStyle(
                            fontSize = 35.sp,
                            fontFamily = FontFamily(Font(R.font.impact)),
                            fontWeight = FontWeight(400),
                            color = Purple80))
                Text(
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
        LazyColumn(modifier = Modifier.padding(paddingValues), userScrollEnabled = true) {
          for (n in news) {
            item {
              Box(
                  modifier =
                      Modifier.padding(16.dp)
                          .shadow(
                              elevation = 10.dp, spotColor = Color.Gray, ambientColor = Color.Gray)
                          .fillMaxSize()
                          .background(
                              color = Color(0xFFFFFFFF),
                              shape = RoundedCornerShape(size = 15.dp))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                      if (n.eventId == "") {
                        /*TODO: Implement the screen when an event is assigned to a news*/
                      } else {

                        var association by remember {
                          mutableStateOf(Association("", "", "", null))
                        }
                        newsViewModel.getNewsAssociation(n.associationId) { association = it }
                        Text(
                            fontSize = 20.sp,
                            text = n.title,
                        )
                        Text(
                            text = n.description,
                        )
                        Text(
                            text = n.date.toString(),
                        )
                        Text(
                            text = association.fullname,
                        )
                        Text(
                            text = n.eventId,
                        )
                      }
                    }
                  }
            }
          }
        }
      }
}
