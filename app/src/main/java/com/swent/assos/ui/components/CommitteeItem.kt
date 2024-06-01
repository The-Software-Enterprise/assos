package com.swent.assos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.view.UserViewModel

@Composable
fun CommitteeItem(userId: String, position: String) {

  val viewModel: UserViewModel = hiltViewModel()
  val user by viewModel.user.collectAsState()

  LaunchedEffect(key1 = Unit) { viewModel.getUser(userId) }

  Box(
      modifier =
          Modifier.background(MaterialTheme.colorScheme.background)
              .fillMaxWidth()
              .height(100.dp)
              .testTag("CommitteeListItem")) {
        Column(
            modifier =
                Modifier.padding(start = 16.dp, top = 20.dp)
                    .align(Alignment.Center)
                    .testTag("CommitteeItemColumn"),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.Start) {
              Text(
                  modifier = Modifier.testTag("CommitteMemeberName"),
                  text = user.firstName + " " + user.lastName,
                  fontSize = 20.sp,
                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                  fontWeight = FontWeight.SemiBold,
                  color = MaterialTheme.colorScheme.onBackground)
              Text(
                  modifier = Modifier.weight(1f).testTag("CommitteMemeberPosition"),
                  text = position,
                  fontSize = 14.sp,
                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                  color = MaterialTheme.colorScheme.onBackground)
            }
      }
}
