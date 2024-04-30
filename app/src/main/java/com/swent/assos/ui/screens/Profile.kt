@file:OptIn(ExperimentalMaterial3Api::class)

package com.swent.assos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.ProfileViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Profile(navigationActions: NavigationActions) {

  val viewModel: ProfileViewModel = hiltViewModel()
  val followedAssociationsList by viewModel.followedAssociations.collectAsState()
  val myAssociations by viewModel.memberAssociations.collectAsState()
  val firstName by viewModel.firstName.collectAsState()
  val lastName by viewModel.lastName.collectAsState()

  val completeName = "$firstName $lastName"

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("ProfileScreen"),
      topBar = {
        TopAppBar(
            modifier = Modifier.testTag("TopBar"),
            title = { Text(text = completeName, modifier = Modifier.testTag("Name")) },
            actions = {
              Icon(
                  painterResource(id = R.drawable.settings),
                  contentDescription = "Map",
                  modifier =
                      Modifier.testTag("SettingsButton").width(40.dp).height(40.dp).clickable {
                        navigationActions.navigateTo(destination = Destinations.SETTINGS)
                      },
                  tint = MaterialTheme.colorScheme.onSurface)
            })
      }) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier =
                Modifier.testTag("ContentSection")
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)) {
              item {
                Text(
                    text = "My associations",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.testTag("MyAssociationSectionTitle"))
                Spacer(modifier = Modifier.height(20.dp))
              }

              items(myAssociations) {
                Card(
                    modifier =
                        Modifier.testTag("MyAssociationItem")
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(50.dp)
                            .clickable {
                              val dest = Destinations.ASSO_MODIFY_PAGE.route + "/${it.id}"
                              navigationActions.navigateTo(dest)
                            },
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background)) {
                      Text(text = it.acronym)
                    }
              }

              item {
                Text(
                    text = "Associations followed",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.testTag("FollowedAssociationSectionTitle"))
                Spacer(modifier = Modifier.height(20.dp))
              }

              items(followedAssociationsList) {
                Card(
                    modifier =
                        Modifier.testTag("FollowedAssociationItem")
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(50.dp)
                            .clickable {
                              val dest = Destinations.ASSO_DETAILS.route + "/${it.id}"
                              navigationActions.navigateTo(dest)
                            },
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background)) {
                      Text(text = it.acronym)
                    }
              }
            }
      }
}