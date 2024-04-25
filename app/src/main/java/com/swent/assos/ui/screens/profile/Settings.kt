package com.swent.assos.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.ui.components.BasicButtonWithIcon
import com.swent.assos.ui.components.PageTitle
import com.swent.assos.ui.components.PageTitleWithGoBack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Settings(navigationActions: NavigationActions) {
  Scaffold(
    modifier = Modifier
      .semantics { testTagsAsResourceId = true }
      .testTag("SettingsScreen"),
    topBar = { PageTitleWithGoBack(title = "Settings", navigationActions) },
   ) { paddingValues ->
    Column(modifier = Modifier
      .padding(paddingValues)
      .fillMaxWidth()) {
      BasicButtonWithIcon("Notification settings", {navigationActions.navigateTo(Destinations.NOTIFICATION_SETTINGS)}, Icons.Default.Notifications)
      BasicButtonWithIcon("Appearance", {navigationActions.navigateTo(Destinations.APPEARANCE)}, Icons.Default.ColorLens)
    }
  }
}
