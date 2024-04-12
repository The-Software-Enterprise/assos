package com.swent.assos.ui.manageAssos

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetCreation(
    navigationActions: NavigationActions,
    onDismiss: () -> Unit,
    assoId: String,
) {
  val scope = rememberCoroutineScope()
  val modalBottomSheetState = rememberModalBottomSheetState()

  fun dismiss() {
    scope
        .launch { modalBottomSheetState.hide() }
        .invokeOnCompletion {
          if (!modalBottomSheetState.isVisible) {
            onDismiss()
          }
        }
  }

  ModalBottomSheet(
      onDismissRequest = { dismiss() },
      modifier = Modifier.padding(bottom = 50.dp).height(200.dp),
      containerColor = Color(0xFFFFFFFF),
      sheetState = modalBottomSheetState,
      dragHandle = { BottomSheetDefaults.DragHandle() },
  ) {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
      Row(
          Modifier.fillMaxWidth().clickable {
            navigationActions.navigateTo(Destinations.CREATE_NEWS.route + "/$assoId")
          },
          verticalAlignment = Alignment.CenterVertically) {
            Image(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(25.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Create a news")
          }
      Spacer(modifier = Modifier.height(20.dp))
      Row(
          Modifier.fillMaxWidth().clickable {
            navigationActions.navigateTo(Destinations.CREATE_EVENT)
          },
          verticalAlignment = Alignment.CenterVertically) {
            Image(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(25.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Create an event")
          }
    }
  }
}
