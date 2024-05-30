package com.swent.assos.ui.screens.manageAsso

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.model.view.PositionViewModel
import com.swent.assos.ui.screens.manageAsso.TopAssoBar

@Composable
fun PosDetails(assoId: String, posId: String, navigationActions: NavigationActions) {

  val assoModel: AssoViewModel = hiltViewModel()
  val association = assoModel.association.collectAsState()
  val viewModel: PositionViewModel = hiltViewModel()
  val position = viewModel.position.collectAsState()

  LaunchedEffect(key1 = Unit) {
    assoModel.getAssociation(assoId)
    viewModel.getPosition(assoId, posId)
  }

  Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = { TopAssoBar(asso = association.value, navigationActions = navigationActions) },
      floatingActionButton = {},
      floatingActionButtonPosition = FabPosition.Center) { paddingValues ->
        LazyColumn {
          item { Text(text = "Position Details", modifier = Modifier.padding(paddingValues)) }
          item {
            Text(text = position.value.title, modifier = Modifier.padding(paddingValues))
            Text(text = position.value.description, modifier = Modifier.padding(paddingValues))
          }

          item { Text(text = "Requirements", modifier = Modifier.padding(paddingValues)) }
          items(position.value.requirements.size) {
            Text(text = position.value.requirements[it], modifier = Modifier.padding(paddingValues))
          }
          item { Text(text = "Responsibilities", modifier = Modifier.padding(paddingValues)) }
          items(position.value.responsibilities.size) {
            Text(
                text = position.value.responsibilities[it],
                modifier = Modifier.padding(paddingValues))
          }
        }
      }
}
