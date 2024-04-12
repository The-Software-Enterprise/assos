package com.swent.assos.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swent.assos.ui.theme.AssosTheme

@Composable
fun ManageAssociation() {
  AssosTheme { ManageAssociationContent() }
}

@Composable
fun ManageAssociationContent() {
  Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
    Text(text = "Manage My Association", modifier = Modifier.padding(16.dp))
    Button(
        onClick = { /* TODO: Implement navigation to event creation page */},
        modifier = Modifier.padding(16.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)) {
          Text("Create Event")
        }
  }
}
