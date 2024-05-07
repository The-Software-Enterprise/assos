@file:OptIn(ExperimentalMaterial3Api::class)

package com.swent.assos.ui.screens.ticket

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.model.data.Ticket
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.ui.components.PageTitleWithGoBack
import java.time.LocalDateTime

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScanTicket(navigationActions: NavigationActions) {

    Scaffold(
        modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("TicketScanScreen"),
        topBar = { PageTitleWithGoBack("Scan a ticket", navigationActions) }) {
        paddingValues ->
        Column(modifier = Modifier.fillMaxWidth().padding(paddingValues).testTag("TicketScanDetails"),
            horizontalAlignment = Alignment.CenterHorizontally,){}
    }
}

