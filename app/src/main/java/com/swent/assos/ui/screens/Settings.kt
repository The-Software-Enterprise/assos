package com.swent.assos.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.ProfileViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Settings(navigationActions: NavigationActions) {
  Column(modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("SettingsScreen")) {
    val viewModel: ProfileViewModel = hiltViewModel()
    Button(
        modifier = Modifier.testTag("LogoutButton"),
        onClick = {
          viewModel.signOut()
          navigationActions.navigateTo(Destinations.LOGIN)
        }) {
          Text("Sign out")
        }
    Text("more to come.")
  }
}
