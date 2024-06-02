package com.swent.assos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.data.Association
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.model.view.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAssoBar(
    asso: Association,
    navigationActions: NavigationActions,
    viewModel: AssoViewModel,
    joinButton: @Composable () -> Unit = {}
) {
  val associationFollowed = viewModel.associationFollowed.collectAsState()
  val profileViewModel: ProfileViewModel = hiltViewModel()

  MediumTopAppBar(
      colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
      modifier = Modifier.testTag("Header"),
      title = {
        Text(
            asso.acronym,
            modifier = Modifier.testTag("Title"),
            style =
                TextStyle(
                    fontSize = 30.sp,
                    lineHeight = 32.sp,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground))
      },
      navigationIcon = {
        Image(
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier.testTag("GoBackButton").clickable { navigationActions.goBack() })
      },
      actions = {
        joinButton()
        if (asso.id != "") {
          AssistChip(
              colors =
                  if (associationFollowed.value)
                      AssistChipDefaults.assistChipColors(
                          containerColor = MaterialTheme.colorScheme.surfaceVariant)
                  else
                      AssistChipDefaults.assistChipColors(
                          containerColor = MaterialTheme.colorScheme.secondary),
              border = null,
              modifier = Modifier.testTag("FollowButton").padding(5.dp),
              onClick = {
                if (associationFollowed.value) {
                  viewModel.unfollowAssociation(asso.id)
                } else {
                  viewModel.followAssociation(asso.id)
                }
                profileViewModel.updateNeeded()
              },
              label = {
                if (associationFollowed.value) {
                  Text(
                      text = "Following",
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight.Medium,
                  )
                } else {
                  Text(
                      text = "Follow",
                      color = MaterialTheme.colorScheme.onSecondary,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight.Medium,
                  )
                }
              },
          )
        }
      })
}
