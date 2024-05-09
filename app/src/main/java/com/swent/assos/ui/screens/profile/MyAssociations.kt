package com.swent.assos.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.ProfileViewModel
import com.swent.assos.ui.components.ListItemAsso
import com.swent.assos.ui.components.PageTitleWithGoBack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MyAssociations(navigationActions: NavigationActions) {

  val viewModel: ProfileViewModel = hiltViewModel()
  val myAssociations by viewModel.memberAssociations.collectAsState()

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("MyAssociationsScreen"),
      topBar = { PageTitleWithGoBack(title = "My Associations", navigationActions) },
  ) { paddingValues ->
    LazyColumn(
        contentPadding = paddingValues,
        modifier =
            Modifier.testTag("ContentSection")
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)) {
          items(myAssociations.size) { k ->
            ListItemAsso(
                asso = myAssociations[k],
                callback = {
                  navigationActions.navigateTo(
                      Destinations.ASSO_MODIFY_PAGE.route + "/${myAssociations[k].id}")
                })
          }
        }
  }
}
