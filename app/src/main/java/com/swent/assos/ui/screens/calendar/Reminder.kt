package com.swent.assos.ui.screens.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.view.CalendarViewModel

@Composable
fun Reminder() {

    val calendarViewModel: CalendarViewModel = hiltViewModel()
    val tomorrowEvents by calendarViewModel.tomorrowEvents.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Reminder",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 26.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(600),
                color = Color(0xFF1E293B),
                letterSpacing = 0.3.sp,
            )
        )
        Text(
            text = "Don't forget schedule for tomorrow",
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 26.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(400),
                color = Color(0xFF575A61),
                letterSpacing = 0.5.sp,
            )
        )
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            userScrollEnabled = true
        ) {
            for (event in tomorrowEvents) {
                item {
                    Box(modifier = Modifier
                        .width(327.dp)
                        .height(64.dp)
                        .background(
                            color = Color(0xFF8572FF),
                            shape = RoundedCornerShape(size = 10.dp)
                        )) {
                        Row {
                            Image(painter = painterResource(R.drawable.calendar), contentDescription = null)
                            Column {
                                Text(
                                    text = "${event.second.title} - ${event.first}",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        lineHeight = 26.sp,
                                        fontFamily = FontFamily(Font(R.font.inter)),
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFFFFFFFF),
                                        letterSpacing = 0.5.sp,
                                    )
                                )
                            }
                        }
                    }
                }
            }


        }

    }
}