package com.swent.assos.ui.screens.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Following(navigationActions: NavigationActions) {

  val viewModel: ProfileViewModel = hiltViewModel()
  val followedAssociationsList by viewModel.followedAssociations.collectAsState()
  val loading by viewModel.loading.collectAsState()
  val update = viewModel.update.collectAsState()

  LaunchedEffect(key1 = Unit) { viewModel.updateUser() }

  LaunchedEffect(key1 = update.value) { viewModel.updateUser() }

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("FollowingScreen"),
      topBar = { PageTitleWithGoBack(title = "Following", navigationActions) },
  ) { paddingValues ->
    LazyColumn(
        contentPadding = paddingValues,
        modifier =
            Modifier.testTag("ContentSection")
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)) {
          if (loading) {
            item { LoadingCircle() }
          } else {
            if (followedAssociationsList.isEmpty()) {
              item { Text(text = stringResource(R.string.NoResult), textAlign = TextAlign.Center) }
            } else {
              items(items = followedAssociationsList, key = { it.id }) {
                ListItemAsso(
                    asso = it,
                    callback = {
                      navigationActions.navigateTo(Destinations.ASSO_DETAILS.route + "/${it.id}")
                    })
              }
            }
          }
        }
  }
}
