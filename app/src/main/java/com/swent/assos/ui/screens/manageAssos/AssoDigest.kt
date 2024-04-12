package com.swent.assos.ui.screens.manageAssos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.swent.assos.R
import com.swent.assos.model.data.Association
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AssoDigest(asso: Association, navigationActions: NavigationActions) {

  var showBottomSheetCreation by remember { mutableStateOf(false) }

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("AssoDigestScreen"),
      topBar = {
        MediumTopAppBar(
            title = { Text(asso.acronym, modifier = Modifier.testTag("AssoTitle")) },
            navigationIcon = {
              Image(
                  imageVector = Icons.Default.ArrowBack,
                  contentDescription = null,
                  modifier =
                      Modifier.testTag("GoBackButton").clickable { navigationActions.goBack() })
            },
            colors =
                TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
        )
      },
      floatingActionButton = {
        Box(
            modifier =
                Modifier.size(50.dp)
                    .clip(RoundedCornerShape(100))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable {
                      // showBottomSheetCreation = true
                      navigationActions.navigateTo(Destinations.CREATE_NEWS.route + "/${asso.id}")
                    }
                    .testTag("CreateButton"),
            contentAlignment = Alignment.Center) {
              Image(
                  imageVector = Icons.Default.Add,
                  contentDescription = null,
                  modifier = Modifier.size(30.dp))
            }
      },
      floatingActionButtonPosition = FabPosition.End,
  ) { paddingValues ->
    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally) {
          Image(
              modifier = Modifier.fillMaxWidth(),
              painter = painterResource(id = R.drawable.logo),
              contentDescription = null)
          Text(text = asso.fullname, modifier = Modifier.testTag("AssoName"))
          Text(text = "Website : " + asso.url, modifier = Modifier.testTag("AssoWebsite"))
        }
  }

  if (showBottomSheetCreation) {
    BottomSheetCreation(navigationActions, { showBottomSheetCreation = false }, asso.id)
  }
}
