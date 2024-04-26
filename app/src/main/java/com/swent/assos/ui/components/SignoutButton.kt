package com.swent.assos.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions

@Composable
// take as input a function that will be called when the user clicks on the button
fun SignoutButton(onclick: () -> Unit, navigationActions: NavigationActions) {
  Button(
      modifier = Modifier.testTag("SignoutButton"),
      onClick = {
        onclick()
        navigationActions.navigateTo(Destinations.LOGIN)
      }) {
        Text("Sign out")
      }
}
