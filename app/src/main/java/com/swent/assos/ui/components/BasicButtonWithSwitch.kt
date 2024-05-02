package com.swent.assos.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swent.assos.R

@Composable
fun BasicButtonWithSwitch(buttonName: String, description: String) {
  Row(
      modifier = Modifier.fillMaxWidth().height(56.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
      verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(16.dp))
        SwitchButton()
        Column {
          Text(
              text = buttonName,
              style =
                  TextStyle(
                      fontSize = 16.sp,
                      lineHeight = 24.sp,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight(500),
                      color = Color(0xFF1D1B20),
                      letterSpacing = 0.5.sp,
                  ))
          Text(
              text = description,
              style =
                  TextStyle(
                      fontSize = 14.sp,
                      lineHeight = 20.sp,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight(400),
                      color = Color(0xFF49454F),
                      letterSpacing = 0.25.sp,
                  ))
        }
      }
}
