package com.swent.assos.ui.manageAssos

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.swent.assos.model.navigation.NavigationActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEvent(navigationActions: NavigationActions) {
  Scaffold(
      topBar = {
        TopAppBar(
            title = { Text("Create an event") },
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
  ) { paddingValues ->
    Column(
        modifier = Modifier.padding(paddingValues),
    ) {}
  }
}
