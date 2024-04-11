package com.swent.assos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.swent.assos.R
import com.swent.assos.model.data.Association
import com.swent.assos.model.navigation.NavigationActions

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AssoDigest(asso: Association, navigationActions: NavigationActions) {
  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("AssoDigestScreen"),
      topBar = {
        MediumTopAppBar(
            title = { Text(asso.acronym, modifier = Modifier.testTag("AssoTitle")) },
            navigationIcon = {
              Image(
                  imageVector = Icons.Default.ArrowBack,
                  contentDescription = null,
                  modifier = Modifier.testTag("GoBackButton").clickable { navigationActions.goBack() })
            })
      }) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally) {
              Image(
                  modifier = Modifier.fillMaxWidth(),
                  painter = painterResource(id = R.drawable.logo),
                  contentDescription = null)
              Text(text = asso.fullname, modifier = Modifier.testTag("AssoName"))
              Text(text = "Website : " + asso.url, modifier = Modifier.testTag("AssoWebsite"))
            }
      }
}
