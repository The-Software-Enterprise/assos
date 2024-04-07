package com.swent.assos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swent.assos.R
import com.swent.assos.model.data.Association
import com.swent.assos.model.view.OverviewViewModel

@Composable
fun Overview(overviewViewModel: OverviewViewModel) {
  val associations by overviewViewModel.filteredAssociations.collectAsState()

  val listState = rememberLazyListState()

  LaunchedEffect(listState) {
    snapshotFlow { listState.layoutInfo.visibleItemsInfo }
        .collect { visibleItems ->
          if (visibleItems.isNotEmpty() && visibleItems.last().index == associations.size - 1) {
            overviewViewModel.loadMoreAssociations()
          }
        }
  }

  Scaffold(
      topBar = {
        Column {
          Row(
              modifier =
                  Modifier.padding(8.dp)
                      .align(Alignment.CenterHorizontally)
                      .wrapContentHeight(Alignment.CenterVertically)) {
                Text(
                    text = "Student",
                    style =
                        TextStyle(
                            fontSize = 35.sp,
                            fontFamily = FontFamily(Font(R.font.impact)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFFD1D2F9)))
                Text(
                    text = "Sphere",
                    style =
                        TextStyle(
                            fontSize = 35.sp,
                            fontFamily = FontFamily(Font(R.font.impact)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFFA3BCF9)))
              }

          TopResearchBar(overviewViewModel = overviewViewModel)
        }
      }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            userScrollEnabled = true,
            state = listState) {
              if (associations.isEmpty()) {
                item { Text(text = "No results were found", textAlign = TextAlign.Center) }
              } else {
                items(items = associations, key = { it.hashCode() }) {
                  ListItemFrom(it)
                  Divider(
                      modifier = Modifier.padding(start = 26.dp, end = 26.dp),
                      color = Color(0xFFC9CAD9))
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
      colors =
          ListItemDefaults.colors(
              headlineColor = Color(0xFF576490),
              overlineColor = Color(0xFF576490),
              supportingColor = Color(0xFF576490),
              trailingIconColor = Color(0xFF576490),
              containerColor = Color.Transparent),
      modifier = Modifier.padding(start = 26.dp, end = 26.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopResearchBar(overviewViewModel: OverviewViewModel) {
  val query by overviewViewModel.researchQuery.collectAsState()
  var isSearching by remember { mutableStateOf(false) }
  val focusManager = LocalFocusManager.current

  Column(
      modifier = Modifier.fillMaxWidth().padding(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally) {
        DockedSearchBar(
            modifier = Modifier.fillMaxWidth(),
            colors = SearchBarDefaults.colors(containerColor = Color(0x50C9CAD9)),
            trailingIcon = {
              if (isSearching) {
                Image(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier =
                        Modifier.clickable {
                          overviewViewModel.filterOnSearch("")
                          focusManager.clearFocus()
                          isSearching = false
                        })
              } else {
                Image(imageVector = Icons.Default.Search, contentDescription = null)
              }
            },
            placeholder = { Text(text = "Search an Association") },
            query = query,
            onQueryChange = { overviewViewModel.filterOnSearch(it) },
            onSearch = {},
            active = false,
            onActiveChange = { isSearching = it }) {}
      }
}
