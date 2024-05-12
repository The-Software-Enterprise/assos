package com.swent.assos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.swent.assos.R
import com.swent.assos.model.data.User
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.model.view.UserViewModel

@Composable
fun NameListItem(userId: String, navigationActions: NavigationActions, /*onCLick: () -> Unit*/) {

    val viewModel: UserViewModel = hiltViewModel()
    val user by viewModel.user.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getUser(userId)
    }


  var buttonText by remember { mutableStateOf("Accept") }
  var isAccepted by remember { mutableStateOf(false) }

  Box(
      modifier =
      Modifier
          .shadow(4.dp, RoundedCornerShape(6.dp))
          .background(MaterialTheme.colorScheme.background)
          .fillMaxWidth()
          .height(20.dp)
          .testTag("NameListItem")) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .testTag("NameItemRow"),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically) {
              Text(
                  text = user.firstName + user.lastName,
                  style = MaterialTheme.typography.bodyMedium,
                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                  fontWeight = FontWeight.Medium,
                  modifier = Modifier
                      .padding(all = 2.dp)
                      .testTag("NameListItemFullName"))
              Button(
                  onClick = {
                    //onCLick()
                    isAccepted = !isAccepted
                    buttonText = if (isAccepted) "Accepted" else "Accept"
                  },
                  modifier =
                  Modifier
                      .padding(end = 8.dp) // Padding to push the button towards the right
                      .testTag("AcceptButton") // Test tag for testing purposes
                  ) {
                    Text(
                        text = buttonText,
                    )
                  }
            }
      }
}
