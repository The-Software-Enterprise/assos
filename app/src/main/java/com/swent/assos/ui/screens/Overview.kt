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
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.swent.assos.R
import com.swent.assos.model.data.Association
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.OverviewViewModel
import com.swent.assos.ui.theme.Purple40
import com.swent.assos.ui.theme.Purple80
import com.swent.assos.ui.theme.PurpleGrey40
import com.swent.assos.ui.theme.PurpleGrey80
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Overview(navigationActions: NavigationActions) {
  val overviewViewModel: OverviewViewModel = hiltViewModel()
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
      modifier = Modifier
          .semantics { testTagsAsResourceId = true }
          .testTag("OverviewScreen"),
      topBar = {
        Column {
          Row(
              modifier =
              Modifier
                  .padding(8.dp)
                  .align(Alignment.CenterHorizontally)
                  .wrapContentHeight(Alignment.CenterVertically)) {
                Text(
                    text = "Student",
                    modifier = Modifier.testTag("AppTitle_1"),
                    style =
                        TextStyle(
                            fontSize = 35.sp,
                            fontFamily = FontFamily(Font(R.font.impact)),
                            fontWeight = FontWeight(400),
                            color = Purple80))
              Text(text ="")
                Text(
                    text = "Sphere",
                    modifier = Modifier.testTag("AppTitle_2"),
                    style =
                        TextStyle(
                            fontSize = 35.sp,
                            fontFamily = FontFamily(Font(R.font.impact)),
                            fontWeight = FontWeight(400),
                            color = Purple40))
              }

          TopResearchBar(overviewViewModel = overviewViewModel)
        }
      }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .testTag("AssoList"),
            userScrollEnabled = true,
            state = listState) {
              if (associations.isEmpty()) {
                item {
                  Text(text = stringResource(R.string.NoResult), textAlign = TextAlign.Center)
                }
              } else {
                items(items = associations, key = { it.hashCode() }) {
                  ListItemFrom(asso = it, navigationActions = navigationActions)
                  Divider(
                      modifier = Modifier.padding(start = 26.dp, end = 26.dp), color = PurpleGrey80)
                }
              }
            }
      }
}

@Composable
fun ListItemFrom(asso: Association, navigationActions: NavigationActions) {
  ListItem(
      headlineContent = { Text(text = asso.acronym) },
      overlineContent = { Text(text = asso.fullname) },
      supportingContent = { Text(text = asso.url) },
      trailingContent = {
        Row {
          Text(text = "more info")
          Image(
              painter = painterResource(id = R.drawable.baseline_arrow_right_24),
              contentDescription = null)
        }
      },
      colors =
          ListItemDefaults.colors(
              headlineColor = PurpleGrey40,
              overlineColor = PurpleGrey40,
              supportingColor = PurpleGrey40,
              trailingIconColor = PurpleGrey40,
              containerColor = Color.Transparent),
      modifier =
      Modifier
          .testTag("AssoListItem")
          .padding(start = 26.dp, end = 26.dp)
          .clickable {
              val dest =
                  Destinations.ASSOCIATION_PAGE.route +
                          "/${asso.id}"
              navigationActions.navigateTo(dest)
          })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopResearchBar(overviewViewModel: OverviewViewModel) {
  val query by overviewViewModel.researchQuery.collectAsState()
  var isSearching by remember { mutableStateOf(false) }
  val focusManager = LocalFocusManager.current

  Column(
      modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally) {
        DockedSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("SearchAsso"),
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
