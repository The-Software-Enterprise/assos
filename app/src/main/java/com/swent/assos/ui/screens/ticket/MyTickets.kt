@file:OptIn(ExperimentalMaterial3Api::class)

package com.swent.assos.ui.screens.ticket

import android.net.Uri
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.swent.assos.model.data.Ticket
import com.swent.assos.model.data.User
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.ProfileViewModel
import com.swent.assos.ui.components.BasicButtonWithIcon
import com.swent.assos.ui.components.PageTitle
import com.swent.assos.ui.components.TicketItem
import java.time.LocalDateTime

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MyTickets(navigationActions: NavigationActions) {

    val exampleTicket = Ticket(
        name = "Balélec Ticket",
        startTime = LocalDateTime.now(),
        banner = Uri.parse("https://scontent-zrh1-1.xx.fbcdn.net/v/t39.30808-6/417379790_892614632321109_331978589442030329_n.jpg?stp=cp6_dst-jpg&_nc_cat=104&ccb=1-7&_nc_sid=5f2048&_nc_ohc=BCPdUFeZuqoQ7kNvgFT7nt-&_nc_ht=scontent-zrh1-1.xx&oh=00_AfA0SUayO8Bt2y2LQJLNHaL8CP7NSV5ChMfmQuuP5fxrHA&oe=663AC822")
    )

    Scaffold(
        modifier = Modifier
            .semantics { testTagsAsResourceId = true }
            .testTag("TicketScreen"),
        topBar = { PageTitle(title = "My Tickets") }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .testTag("ContentSection")) {
            TicketItem(ticket = exampleTicket, navigationActions = navigationActions)
        }
    }
}

