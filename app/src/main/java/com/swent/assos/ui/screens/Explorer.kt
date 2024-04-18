package com.swent.assos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.data.Association
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.Explorer
import com.swent.assos.ui.theme.PurpleGrey40

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Explorer(navigationActions: NavigationActions) {
  val explorer: Explorer = hiltViewModel()
  val associations by explorer.filteredAssociations.collectAsState()

  val listState = rememberLazyListState()

  LaunchedEffect(listState) {
    snapshotFlow { listState.layoutInfo.visibleItemsInfo }
        .collect { visibleItems ->
          if (visibleItems.isNotEmpty() && visibleItems.last().index == associations.size - 1) {
            explorer.loadMoreAssociations()
          }
        }
  }

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("ExplorerScreen"),
      topBar = {
        Column {
          Row(
              modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(8.dp),
              verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Explorer",
                    style =
                        TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    modifier = Modifier.padding(start = 20.dp))
              }
          TopResearchBar(explorer = explorer)
        }
      }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).testTag("AssoList"),
            userScrollEnabled = true,
            state = listState) {
              if (associations.isEmpty()) {
                item {
                  Text(text = stringResource(R.string.NoResult), textAlign = TextAlign.Center)
                }
              } else {
                items(items = associations, key = { it.id }) {
                  ListItemAsso(asso = it, navigationActions = navigationActions)
                  Divider(
                      modifier = Modifier.padding(horizontal = 20.dp),
                      color = MaterialTheme.colorScheme.surface)
                }
              }
            }
      }
}

@Composable
fun ListItemAsso(asso: Association, navigationActions: NavigationActions) {
  ListItem(
      modifier =
          Modifier.fillMaxWidth()
              .testTag("AssoListItem")
              .clickable {
                val dest =
                    Destinations.ASSO_DETAILS.route + "/${asso.id
                      }"
                navigationActions.navigateTo(dest)
              }
              .padding(start = 20.dp, top = 5.dp, bottom = 5.dp, end = 5.dp),
      headlineContent = {
        Text(
            text = asso.acronym,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black)
      },
      supportingContent = {
        Text(text = asso.fullname, fontSize = 12.sp, fontWeight = FontWeight.Light)
      },
      leadingContent = {
        Image(
            modifier = Modifier.size(40.dp),
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null)
      },
      trailingContent = {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.baseline_arrow_right_24),
            contentDescription = null)
      },
      colors =
          ListItemDefaults.colors(
              headlineColor = PurpleGrey40,
              overlineColor = PurpleGrey40,
              supportingColor = PurpleGrey40,
              trailingIconColor = PurpleGrey40,
              containerColor = Color.Transparent),
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopResearchBar(explorer: Explorer) {
  val query by explorer.researchQuery.collectAsState()
  var isSearching by remember { mutableStateOf(false) }
  val focusManager = LocalFocusManager.current

  Column(
      modifier = Modifier.fillMaxWidth().padding(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally) {
        DockedSearchBar(
            modifier = Modifier.fillMaxWidth().testTag("SearchAsso"),
            colors = SearchBarDefaults.colors(containerColor = Color(0x50C9CAD9)),
            trailingIcon = {
              if (isSearching) {
                Image(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier =
                        Modifier.clickable {
                          explorer.filterOnSearch("")
                          focusManager.clearFocus()
                          isSearching = false
                        })
              } else {
                Image(imageVector = Icons.Default.Search, contentDescription = null)
              }
            },
            placeholder = { Text(text = "Search an Association") },
            query = query,
            onQueryChange = { explorer.filterOnSearch(it) },
            onSearch = {},
            active = false,
            onActiveChange = { isSearching = it }) {}
      }
}
