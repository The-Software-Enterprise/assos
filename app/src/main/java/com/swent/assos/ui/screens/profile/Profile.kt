@file:OptIn(ExperimentalMaterial3Api::class)

package com.swent.assos.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
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
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.ProfileViewModel
import com.swent.assos.ui.components.BasicButtonWithIcon
import com.swent.assos.ui.components.PageTitle

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Profile(navigationActions: NavigationActions) {

  var showLogOut by remember { mutableStateOf(false) }

  val viewModel: ProfileViewModel = hiltViewModel()
  val firstName by viewModel.firstName.collectAsState()
  val lastName by viewModel.lastName.collectAsState()

  val completeName = "$firstName $lastName"

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("ProfileScreen"),
      topBar = { PageTitle(title = "Profile") }) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxWidth().testTag("ContentSection")) {
              UserNameDisplay(completeName)
              BasicButtonWithIcon(
                  "My Associations",
                  { navigationActions.navigateTo(Destinations.MY_ASSOCIATIONS) },
                  Icons.Default.Home)
              BasicButtonWithIcon(
                  "Following Associations",
                  { navigationActions.navigateTo(Destinations.FOLLOWING) },
                  Icons.Default.Add)
              BasicButtonWithIcon(
                  "Settings",
                  { navigationActions.navigateTo(Destinations.SETTINGS) },
                  Icons.Default.Settings)
              BasicButtonWithIcon("Log Out", { showLogOut = true }, Icons.Default.Logout)

              if (showLogOut) {
                Logout(onConfirm = { viewModel.signOut() }, onDismiss = { showLogOut = false })
              }
            }
      }
}

@Composable
fun UserNameDisplay(name: String) {
  Row(
      modifier =
          Modifier.fillMaxWidth()
              .height(56.dp)
              .padding(start = 28.dp, top = 18.dp, end = 28.dp, bottom = 18.dp),
      horizontalArrangement = Arrangement.Start,
      verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = name,
            style =
                TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF49454F),
                    letterSpacing = 0.1.sp,
                ),
            modifier = Modifier.height(20.dp).testTag("Name"))
      }
}

@Composable
fun Logout(onConfirm: () -> Unit, onDismiss: () -> Unit) {
  AlertDialog(
      modifier = Modifier.testTag("LogoutDialog"),
      onDismissRequest = onDismiss,
      title = {
        Text(
            text = "Log out",
            modifier = Modifier.testTag("LogoutTitle"),
            style =
                TextStyle(
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF1D1B20),
                ))
      },
      containerColor = Color.White,
      text = {
        Text(
            modifier = Modifier.testTag("LogoutText"),
            text = "You will be returned to the login screen",
            style =
                TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF49454F),
                    letterSpacing = 0.25.sp,
                ))
      },
      confirmButton = {
        TextButton(onClick = onConfirm, modifier = Modifier.testTag("LogoutConfirmButton")) {
          Text(
              text = "Log out",
              style =
                  TextStyle(
                      fontSize = 14.sp,
                      lineHeight = 20.sp,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight(500),
                      color = Color(0xFF6750A4),
                      textAlign = TextAlign.Center,
                      letterSpacing = 0.1.sp,
                  ))
        }
      },
      dismissButton = {
        TextButton(onClick = onDismiss, modifier = Modifier.testTag("LogoutCancelButton")) {
          Text(
              text = "Cancel",
              style =
                  TextStyle(
                      fontSize = 14.sp,
                      lineHeight = 20.sp,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight(600),
                      color = Color(0xFF6750A4),
                      textAlign = TextAlign.Center,
                      letterSpacing = 0.1.sp,
                  ))
        }
      })
}