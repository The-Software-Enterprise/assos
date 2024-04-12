package com.swent.assos.ui.screens.manageAssos

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.CreateNewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNews(
    navigationActions: NavigationActions,
    associationId: String,
    isEdit: Boolean = false
) {
  val viewModel: CreateNewsViewModel = hiltViewModel()

  val news by viewModel.news.collectAsState()

  Scaffold(
      modifier = Modifier.fillMaxSize().testTag("CreateNewsScreen"),
      topBar = {
        TopAppBar(
            title = { Text("Create a news") },
            navigationIcon = {
              Image(
                  imageVector = Icons.Default.ArrowBack,
                  contentDescription = null,
                  modifier =
                      Modifier.testTag("GoBackButton").clickable { navigationActions.goBack() })
            },
            colors =
                TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
        )
      },
  ) { paddingValues ->
    LazyColumn(
        modifier =
            Modifier.padding(top = 10.dp, start = 26.dp, end = 26.dp).fillMaxSize().testTag("Form"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = paddingValues,
    ) {
      item {
        OutlinedTextField(
            value = news.title,
            onValueChange = { viewModel.setTitle(it) },
            modifier = Modifier.fillMaxWidth().height(64.dp).testTag("InputTitle"),
            label = { Text(text = "Title") },
            placeholder = { Text(text = "Title of the news") },
            textStyle = MaterialTheme.typography.bodyLarge,
            colors =
                TextFieldDefaults.colors(
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                ),
            singleLine = true,
        )
      }
      item {
        OutlinedTextField(
            value = news.description,
            onValueChange = { viewModel.setDescription(it) },
            modifier = Modifier.fillMaxWidth().height(150.dp).testTag("InputDescription"),
            label = { Text(text = "Description") },
            placeholder = { Text(text = "Description of the news") },
            singleLine = false,
            textStyle = MaterialTheme.typography.bodyLarge,
            colors =
                TextFieldDefaults.colors(
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                ),
        )
      }
      item {
        FilledIconButton(
            onClick = { viewModel.createNews(associationId, navigationActions) },
            modifier =
                Modifier.fillMaxWidth()
                    .padding(top = 10.dp, bottom = if (isEdit) 0.dp else 40.dp)
                    .height(40.dp)
                    .testTag("ButtonSave"),
            shape = RoundedCornerShape(10.dp),
            colors =
                IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )) {
              Text(
                  text = if (isEdit) "Save" else "Create",
                  style = MaterialTheme.typography.labelLarge,
                  color = MaterialTheme.colorScheme.onPrimary)
            }
      }
    }
  }
}
