package com.swent.assos.ui.screens.ticket

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.TicketViewModel
import com.swent.assos.ui.components.PageTitle
import com.swent.assos.ui.components.PageTitleWithGoBack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateTicket(navigationActions: NavigationActions) {

    Scaffold(
        modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("TicketScreen"),
        topBar = { PageTitleWithGoBack(title = "Create a Ticket", navigationActions = navigationActions) },
        ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(paddingValues).testTag("Form"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}