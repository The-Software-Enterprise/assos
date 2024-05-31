package com.swent.assos.ui.screens.manageAsso

import android.widget.Button
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.model.view.PositionViewModel

@Composable
fun PosDetails(assoId: String, posId: String, navigationActions: NavigationActions) {
  val context = LocalContext.current
  val assoModel: AssoViewModel = hiltViewModel()
  val association by assoModel.association.collectAsState()
  val viewModel: PositionViewModel = hiltViewModel()
  val position by viewModel.position.collectAsState()
  val canDelete by viewModel.canDelete.collectAsState()

  LaunchedEffect(key1 = assoId) {
    assoModel.getAssociation(assoId)
    viewModel.checkCanDelete(assoId)
  }

    LaunchedEffect(key1 = assoId, key2 = posId) {
        viewModel.getPosition(assoId, posId)
    }

  Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = { TopAssoBar(asso = association, navigationActions = navigationActions) },
      floatingActionButton = {
        if (canDelete) {
          Button(
              onClick = {
                viewModel.deletePosition(
                    assoId,
                    posId,
                    {
                      navigationActions
                      Toast.makeText(context, "The position has been deleted", Toast.LENGTH_SHORT)
                          .show()
                    },
                    { Toast.makeText(context, "An error occurred: $it", Toast.LENGTH_LONG).show() })
                navigationActions.goBack()
              }) {
                Row {
                  Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
                  Spacer(modifier = Modifier.width(5.dp))
                  Text("Delete", modifier = Modifier.padding(top = 2.dp))
                }
              }
        }
      },
      floatingActionButtonPosition = FabPosition.Center) { paddingValues ->
        LazyColumn {
          item { Text(text = "Position Details", modifier = Modifier.padding(paddingValues)) }
          item {
            Text(text = position.title, modifier = Modifier.padding(paddingValues))
            Text(text = position.description, modifier = Modifier.padding(paddingValues))
          }

          item { Text(text = "Requirements", modifier = Modifier.padding(paddingValues)) }
          items(position.requirements.size) {
            Text(text = position.requirements[it], modifier = Modifier.padding(paddingValues))
          }
          item { Text(text = "Responsibilities", modifier = Modifier.padding(paddingValues)) }
          items(position.responsibilities.size) {
            Text(text = position.responsibilities[it], modifier = Modifier.padding(paddingValues))
          }
        }
      }
}
