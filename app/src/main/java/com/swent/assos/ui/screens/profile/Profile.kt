@file:OptIn(ExperimentalMaterial3Api::class)

package com.swent.assos.ui.screens.profile

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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.swent.assos.R
import com.swent.assos.model.data.Association
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Profile(navigationActions: NavigationActions) {

  val firstname = "Maximilien"
  val surname = "GRIDEL"

  val completeName = "$firstname $surname"

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("ProfileScreen"),
      topBar = {
        TopAppBar(
            title = { Text(text = completeName) },
            actions = {
              // Ajouter ici l'icône des paramètres

              Icon(
                  painterResource(id = R.drawable.settings),
                  contentDescription = "Map",
                  modifier =
                      Modifier.width(40.dp).height(40.dp).clickable {
                        navigationActions.navigateTo(destination = Destinations.SETTINGS)
                      },
                  tint = MaterialTheme.colorScheme.onSurface)
            })
      }) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(16.dp)) {
              item {
                Text(text = "My associations", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(20.dp))
              }

              items(myAssociations) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp).height(50.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background)) {
                      Text(text = it.acronym)
                    }
              }

              item {
                Text(text = "Associations followed", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(20.dp))
              }

              items(associationsFollowed) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp).height(50.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background)) {
                      Text(text = it.acronym)
                    }
              }
            }
      }
}

// TODO delete
val myAssociations =
    listOf(
        Association(
            acronym = "ACRONYM",
            fullname = "NAME",
            url = "URL",
            description = "",
        ),
        Association(
            acronym = "ACRONYM",
            fullname = "NAME",
            url = "URL",
            description = "",
        ))

val associationsFollowed =
    listOf(
        Association(
            acronym = "ACRONYM",
            fullname = "NAME",
            url = "URL",
            description = "",
        ),
        Association(
            acronym = "ACRONYM",
            fullname = "NAME",
            url = "URL",
            description = "",
        ),
        Association(
            acronym = "ACRONYM",
            fullname = "NAME",
            url = "URL",
            description = "",
        ),
        Association(
            acronym = "ACRONYM",
            fullname = "NAME",
            url = "URL",
            description = "",
        ),
        Association(
            acronym = "ACRONYM",
            fullname = "NAME",
            url = "URL",
            description = "",
        ),
        Association(
            acronym = "ACRONYM",
            fullname = "NAME",
            url = "URL",
            description = "",
        ),
        Association(
            acronym = "ACRONYM",
            fullname = "NAME",
            url = "URL",
            description = "",
        ),
        Association(
            acronym = "ACRONYM",
            fullname = "NAME",
            url = "URL",
            description = "",
        ),
        Association(
            acronym = "ACRONYM",
            fullname = "NAME",
            url = "URL",
            description = "",
        ),
        Association(
            acronym = "ACRONYM",
            fullname = "NAME",
            url = "URL",
            description = "",
        ))