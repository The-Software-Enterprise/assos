package com.swent.assos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swent.assos.R
import com.swent.assos.model.navigation.NavigationActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageTitleWithGoBack(
    title: String,
    navigationActions: NavigationActions,
    actionButton: @Composable RowScope.() -> Unit = {}
) {
  TopAppBar(
      title = {
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
          Text(
              text = title,
              style =
                  TextStyle(
                      fontSize = 24.sp,
                      lineHeight = 20.sp,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight.SemiBold,
                      color = MaterialTheme.colorScheme.onBackground),
              modifier = Modifier.testTag("PageTitle"))
        }
      },
      navigationIcon = {
        Image(
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            imageVector = Icons.Default.ArrowBackIos,
            contentDescription = null,
            modifier =
                Modifier.testTag("GoBackButton")
                    .clip(RoundedCornerShape(100))
                    .clickable { navigationActions.goBack() }
                    .size(20.dp))
      },
      colors =
          TopAppBarDefaults.mediumTopAppBarColors(
              containerColor = MaterialTheme.colorScheme.background),
      actions = { actionButton() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageTitleWithGoBack(
    title: @Composable () -> Unit = {},
    navigationActions: NavigationActions,
    actionButton: @Composable RowScope.() -> Unit = {}
) {
  TopAppBar(
      title = title,
      navigationIcon = {
        Image(
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            imageVector = Icons.Default.ArrowBackIos,
            contentDescription = null,
            modifier =
                Modifier.testTag("GoBackButton")
                    .clip(RoundedCornerShape(100))
                    .clickable { navigationActions.goBack() }
                    .size(20.dp))
      },
      colors =
          TopAppBarDefaults.mediumTopAppBarColors(
              containerColor = MaterialTheme.colorScheme.background),
      actions = { actionButton() })
}
