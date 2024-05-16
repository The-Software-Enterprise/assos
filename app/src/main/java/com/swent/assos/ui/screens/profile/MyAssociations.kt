package com.swent.assos.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.ProfileViewModel
import com.swent.assos.ui.components.ListItemAsso
import com.swent.assos.ui.components.LoadingCircle
import com.swent.assos.ui.components.PageTitleWithGoBack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MyAssociations(navigationActions: NavigationActions) {

  val viewModel: ProfileViewModel = hiltViewModel()
  val myAssociations by viewModel.memberAssociations.collectAsState()
  val loading by viewModel.loading.collectAsState()

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("MyAssociationsScreen"),
      topBar = { PageTitleWithGoBack(title = "My Associations", navigationActions) },
  ) { paddingValues ->
    LazyColumn(
        contentPadding = paddingValues,
        modifier =
            Modifier.testTag("ContentSection")
                .background(MaterialTheme.colorScheme.background )
                .padding(16.dp)) {
          if (loading) {
            item { LoadingCircle() }
          } else {
            if (myAssociations.isEmpty()) {
              item { Text(text = stringResource(R.string.NoResult), textAlign = TextAlign.Center) }
            } else {
              items(items = myAssociations, key = { it.id }) {
                ListItemAsso(
                    asso = it,
                    callback = {
                      navigationActions.navigateTo(
                          Destinations.ASSO_MODIFY_PAGE.route + "/${it.id}")
                    })
              }
            }
          }
        }
  }
}
