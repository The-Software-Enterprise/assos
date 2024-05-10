package com.swent.assos.ui.screens.manageAsso.createEvent.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.swent.assos.model.data.Event
import com.swent.assos.model.view.EventViewModel
import kotlinx.coroutines.launch

@Composable
fun FloatingButtons(viewModel: EventViewModel, lazyListState: LazyListState) {
  val coroutineScope = rememberCoroutineScope()
  val event by viewModel.event.collectAsState()

  Column {
    FloatingActionButton(
        modifier = Modifier.testTag("AddTextField"),
        onClick = {
          viewModel.addField(Event.Field.Text("", ""))
          coroutineScope.launch { lazyListState.scrollToItem(event.fields.size + 2) }
        },
        shape = RoundedCornerShape(size = 16.dp)) {
          Image(imageVector = Icons.Default.TextFields, contentDescription = null)
        }
    Spacer(modifier = Modifier.size(16.dp))
    FloatingActionButton(
        modifier = Modifier.testTag("AddImageField"),
        onClick = {
          viewModel.addField(Event.Field.Image(emptyList()))
          coroutineScope.launch { lazyListState.scrollToItem(event.fields.size + 2) }
        },
        shape = RoundedCornerShape(size = 16.dp)) {
          Image(imageVector = Icons.Default.PhotoLibrary, contentDescription = null)
        }
  }
}
