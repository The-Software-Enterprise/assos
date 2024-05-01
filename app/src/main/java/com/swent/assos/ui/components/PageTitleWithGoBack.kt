package com.swent.assos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swent.assos.R
import com.swent.assos.model.navigation.NavigationActions

@Composable
fun PageTitleWithGoBack(title: String, navigationActions: NavigationActions) {
  Column {
    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(20.dp).testTag("TopBar"),
        verticalAlignment = Alignment.CenterVertically) {
          Image(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "goBack",
              modifier =
                  Modifier.width(24.dp)
                      .height(24.dp)
                      .clickable { navigationActions.goBack() }
                      .testTag("GoBackButton"))
          Text(
              text = title,
              style =
                  TextStyle(
                      fontSize = 24.sp,
                      lineHeight = 32.sp,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight(600),
                      color = Color.Black),
              modifier = Modifier.padding(10.dp).testTag("PageTitle"))
        }
  }
}
