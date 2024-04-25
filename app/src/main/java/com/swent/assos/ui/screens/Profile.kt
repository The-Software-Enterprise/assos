@file:OptIn(ExperimentalMaterial3Api::class)

package com.swent.assos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.ProfileViewModel
import com.swent.assos.ui.components.PageTitle

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Profile(navigationActions: NavigationActions) {

  val viewModel: ProfileViewModel = hiltViewModel()
  val followedAssociationsList by viewModel.followedAssociations.collectAsState()
  val myAssociations by viewModel.memberAssociations.collectAsState()
  val firstName by viewModel.firstName.collectAsState()
  val lastName by viewModel.lastName.collectAsState()

  val completeName = "$firstName $lastName"

  Scaffold(
      modifier = Modifier
          .semantics { testTagsAsResourceId = true }
          .testTag("ProfileScreen"),
      topBar = { PageTitle(title = "Profile") }) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()) {
          UserNameDisplay(completeName)
            UserButtons("My Associations", navigationActions, Icons.Default.Home, Destinations.SETTINGS)
            UserButtons("Following Associations", navigationActions, Icons.Default.Add, Destinations.SETTINGS)
            UserButtons("Settings", navigationActions, Icons.Default.Settings, Destinations.SETTINGS)
            UserButtons("Logout", navigationActions, Icons.Default.Logout, Destinations.SETTINGS)
        }
      }
}

@Composable
fun UserNameDisplay(name: String) {
  Row(
      modifier =
      Modifier
          .fillMaxWidth()
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
            modifier = Modifier
                .width(106.dp)
                .height(20.dp)
                .testTag("Name"))
      }
}

@Composable
fun UserButtons(buttonName: String, navigationActions: NavigationActions, icon: ImageVector, destination: Destinations) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { navigationActions.navigateTo(destination) },
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(16.dp))
        Image(imageVector = icon, contentDescription = buttonName, modifier = Modifier
            .width(24.dp)
            .height(24.dp))
        Text(
            text = buttonName,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                fontWeight = FontWeight(500),
                color = Color(0xFF49454F),
                letterSpacing = 0.1.sp,
            )
        )
    }

}
