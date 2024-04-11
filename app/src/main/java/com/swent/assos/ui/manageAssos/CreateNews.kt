package com.swent.assos.ui.manageAssos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.swent.assos.model.navigation.NavigationActions

@Composable
fun CreateNews(navigationActions: NavigationActions) {
  Scaffold { paddingValues ->
    Column(
        modifier = Modifier.padding(paddingValues),
    ) {}
  }
}
