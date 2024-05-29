package com.swent.assos.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.ui.components.PageTitleWithGoBack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Saved(navigationActions: NavigationActions) {

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("SavedScreen"),
      topBar = { PageTitleWithGoBack(title = "Saved", navigationActions) },
  ) { paddingValues ->
    LazyColumn(
        contentPadding = paddingValues,
        modifier =
            Modifier.testTag("ContentSection")
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)) {}
  }
}
