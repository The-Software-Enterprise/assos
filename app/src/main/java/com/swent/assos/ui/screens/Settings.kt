package com.swent.assos.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.ProfileViewModel
import com.swent.assos.ui.components.SignoutButton

@Composable
fun Settings(navigationActions: NavigationActions) {
  val viewModel: ProfileViewModel = hiltViewModel()
  SignoutButton({ viewModel.signOut() }, navigationActions = navigationActions)

  Text("Settings, not implemnted yet.")
}
