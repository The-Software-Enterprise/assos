package com.swent.assos.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.ProfileViewModel
import com.swent.assos.ui.components.HomeItem
import com.swent.assos.ui.components.LoadingCircle
import com.swent.assos.ui.components.PageTitleWithGoBack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Saved(navigationActions: NavigationActions) {

  val viewModel: ProfileViewModel = hiltViewModel()
  val loading by viewModel.loading.collectAsState()
  val events by viewModel.savedEvents.collectAsState()
  val news by viewModel.savedNews.collectAsState()

  LaunchedEffect(key1 = Unit) { viewModel.updateUser() }

  var selectedOption by remember { mutableStateOf("Events") }

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("SavedScreen"),
      topBar = {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
              PageTitleWithGoBack(title = "Saved", navigationActions)
              SegmentedControlButton(
                  options = listOf("Events", "News"),
                  selectedOption = selectedOption,
                  onOptionSelected = { selectedOption = it })
            }
      },
  ) { paddingValues ->
    LazyColumn(
        contentPadding = paddingValues,
        modifier =
            Modifier.testTag("ContentSection")
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)) {
          if (selectedOption == "Events") {
            if (loading) {
              item { LoadingCircle() }
            } else {
              if (events.isEmpty()) {
                item {
                  Text(text = stringResource(R.string.NoResult), textAlign = TextAlign.Center)
                }
              } else {
                items(items = events, key = { it.id }) {
                  HomeItem(id = it.id, navigationActions = navigationActions)
                }
              }
            }
          } else {
            if (loading) {
              item { LoadingCircle() }
            } else {
              if (news.isEmpty()) {
                item {
                  Text(text = stringResource(R.string.NoResult), textAlign = TextAlign.Center)
                }
              } else {
                items(items = news, key = { it.id }) {
                  HomeItem(id = it.id, navigationActions = navigationActions)
                }
              }
            }
          }
        }
  }
}

@Composable
fun SegmentedControlButton(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
  Row(
      modifier =
          Modifier.padding(8.dp)
              .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
              .padding(2.dp),
      horizontalArrangement = Arrangement.Center) {
        options.forEach { option ->
          val isSelected = selectedOption == option
          Text(
              text = option,
              modifier =
                  Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                      .background(
                          color =
                              if (isSelected) MaterialTheme.colorScheme.primary
                              else MaterialTheme.colorScheme.surface,
                          shape = RoundedCornerShape(16.dp))
                      .clickable { onOptionSelected(option) }
                      .padding(horizontal = 16.dp, vertical = 8.dp),
              color =
                  if (isSelected) MaterialTheme.colorScheme.onPrimary
                  else MaterialTheme.colorScheme.onSurface,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
        }
      }
}
