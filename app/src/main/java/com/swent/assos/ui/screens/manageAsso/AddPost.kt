package com.swent.assos.ui.screens.manageAsso

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.data.Post
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPost(assoId: String, navigationActions: NavigationActions) {

    val viewModel: AssoViewModel = hiltViewModel()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageURL by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add post") },
                navigationIcon = {
                Image(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.testTag("backButton").clickable{ navigationActions.goBack() } )
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") }
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") }
            )
            OutlinedTextField(
                value = imageURL,
                onValueChange = { imageURL = it },
                label = { Text("Image URL") }
            )
            Button(
                onClick = {
                    viewModel.addPost(Post(title, description, imageURL))
                    navigationActions.goBack() },
                enabled = title.isNotEmpty() && description.isNotEmpty() && imageURL.isNotEmpty()) {
                Text(text = "Add post")
            }
        }
    }
}