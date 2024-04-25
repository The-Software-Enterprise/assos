package com.swent.assos.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.ui.components.BasicButtonWithIcon
import com.swent.assos.ui.components.PageTitleWithGoBack
import com.swent.assos.ui.components.BasicButtonWithSwitch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NotificationSettings(navigationActions: NavigationActions) {
    Scaffold(
        modifier = Modifier
            .semantics { testTagsAsResourceId = true }
            .testTag("NotificationSettingsScreen"),
        topBar = { PageTitleWithGoBack(title = "Notification Settings", navigationActions) },
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()) {
            BasicButtonWithSwitch("Allow notifications", "Allow the app to send notifications")
            BasicButtonWithSwitch("Alert to staff or join associations", "Be notified on new open positions")
        }
    }
}