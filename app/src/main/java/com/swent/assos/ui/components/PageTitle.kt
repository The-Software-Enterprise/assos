package com.swent.assos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swent.assos.R
import com.swent.assos.ui.screens.*
import com.swent.assos.ui.theme.Purple40
import com.swent.assos.ui.theme.Purple80

@Composable
fun StudentSphereTitle() {
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
}
