package com.swent.assos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swent.assos.model.data.Association
import com.swent.assos.model.view.OverviewViewModel

@Composable
fun Overview(overviewViewModel: OverviewViewModel) {
  val associations = overviewViewModel.allAssociations.collectAsState()

  Scaffold { paddingValues ->
    LazyColumn(modifier = Modifier.padding(paddingValues), userScrollEnabled = true) {
      if (associations.value.isEmpty()) {
        item { Text(text = "No results were found", textAlign = TextAlign.Center) }
      } else {
        items(items = associations.value, key = {it.hashCode()}) {
          ListItemFrom(it)
          Divider(modifier = Modifier.padding(start = 26.dp, end = 26.dp))
        }
      }
    }
  }
}

@Composable
fun ListItemFrom(asso: Association) {
  ListItem(
    headlineContent = { Text(text = asso.acronym) },
    overlineContent = { Text(text = asso.fullname) },
    supportingContent = { Text(text = asso.url) },
    trailingContent = {
      Row {
        Text(text = "more info")
        Image(imageVector = Icons.Filled.PlayArrow, contentDescription = null)
      }
    },
    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
    modifier =
    Modifier.padding(start = 26.dp, end = 26.dp)
  )
}