package com.swent.assos.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.ui.components.BasicButtonWithSwitch
import com.swent.assos.ui.components.PageTitleWithGoBack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Appearance(navigationActions: NavigationActions) {
  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("AppearanceScreen"),
      topBar = { PageTitleWithGoBack(title = "Appearance", navigationActions) },
  ) { paddingValues ->
    Column(modifier = Modifier.padding(paddingValues).fillMaxWidth()) {
      BasicButtonWithSwitch("Dark Mode", "Use a darker color palette")
    }
  }
}
