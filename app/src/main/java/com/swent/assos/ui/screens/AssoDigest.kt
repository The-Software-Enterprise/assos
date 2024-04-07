package com.swent.assos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.swent.assos.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AssoDigest() {
  Scaffold(
      topBar = {
        MediumTopAppBar(
            title = { Text("Name of the Asso") },
            navigationIcon = {
              Image(
                  imageVector = Icons.Default.ArrowBack,
                  contentDescription = null,
                  modifier = Modifier.clickable {})
            })
      }) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally) {
              Image(
                  modifier = Modifier.fillMaxWidth(),
                  painter = painterResource(id = R.drawable.logo),
                  contentDescription = null)
              Text(
                  text =
                      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
              )
            }
      }
}
