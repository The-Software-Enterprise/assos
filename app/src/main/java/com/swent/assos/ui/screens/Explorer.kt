package com.swent.assos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.data.Association
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.ExplorerViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Explorer(navigationActions: NavigationActions) {
  val explorerViewModel: ExplorerViewModel = hiltViewModel()
  val associations by explorerViewModel.filteredAssociations.collectAsState()

  val listState = rememberLazyListState()

  LaunchedEffect(listState) {
    snapshotFlow { listState.layoutInfo.visibleItemsInfo }
        .collect { visibleItems ->
          if (visibleItems.isNotEmpty() && visibleItems.last().index == associations.size - 1) {
            explorerViewModel.loadMoreAssociations()
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
                            fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                            fontWeight = FontWeight.Bold,
                        ),
                    modifier = Modifier.padding(start = 20.dp))
              }
          TopResearchBar(explorerViewModel = explorerViewModel)
        }
      }) { paddingValues ->
        LazyColumn(
            modifier =
                Modifier.padding(paddingValues)
                    .testTag("AssoList")
                    .background(color = MaterialTheme.colorScheme.background),
            userScrollEnabled = true,
            state = listState) {
              if (associations.isEmpty()) {
                item {
                  Text(text = stringResource(R.string.NoResult), textAlign = TextAlign.Center)
                }
              } else {
                item { Spacer(modifier = Modifier.height(5.dp)) }
                items(items = associations, key = { it.id }) {
                  ListItemAsso(asso = it, navigationActions = navigationActions)
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
              .padding(5.dp)
              .background(color = Color.White, shape = RoundedCornerShape(size = 15.dp))
              .testTag("AssoListItem")
              .clickable {
                val dest =
                    Destinations.ASSO_DETAILS.route +
                        "/${
                  asso.id
              }"
                navigationActions.navigateTo(dest)
              }
              .padding(start = 20.dp, top = 5.dp, bottom = 5.dp, end = 5.dp),
      headlineContent = {
        Text(
            text = asso.acronym,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
            fontWeight = FontWeight.Bold,
            color = Color.Black)
      },
      supportingContent = {
        Text(
            text = asso.fullname,
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
            fontWeight = FontWeight.Light)
      },
      leadingContent = {
        Image(
            modifier = Modifier.size(40.dp),
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null)
      },
      colors =
          ListItemDefaults.colors(
              headlineColor = MaterialTheme.colorScheme.secondary,
              overlineColor = MaterialTheme.colorScheme.secondary,
              supportingColor = MaterialTheme.colorScheme.secondary,
              trailingIconColor = MaterialTheme.colorScheme.secondary,
              containerColor = Color.Transparent),
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopResearchBar(explorerViewModel: ExplorerViewModel) {
  val query by explorerViewModel.researchQuery.collectAsState()
  var isSearching by remember { mutableStateOf(false) }
  val focusManager = LocalFocusManager.current

  Column(
      modifier =
          Modifier.fillMaxWidth()
              .padding(10.dp)
              .background(
                  color = MaterialTheme.colorScheme.background,
                  shape = RoundedCornerShape(size = 28.dp)),
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
                          explorerViewModel.filterOnSearch("")
                          focusManager.clearFocus()
                          isSearching = false
                        })
              } else {
                Image(imageVector = Icons.Default.Search, contentDescription = null)
              }
            },
            leadingIcon = { Image(imageVector = Icons.Default.Menu, contentDescription = null) },
            placeholder = { Text(text = "Search an Association") },
            query = query,
            onQueryChange = { explorerViewModel.filterOnSearch(it) },
            onSearch = {},
            active = false,
            onActiveChange = { isSearching = it }) {}
      }
}
