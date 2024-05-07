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
fun TicketDetails(navigationActions: NavigationActions) {

  val ticket =
      Ticket(
          name = "Balélec Ticket",
          startTime = LocalDateTime.now(),
          banner =
              Uri.parse(
                  "https://scontent-zrh1-1.xx.fbcdn.net/v/t39.30808-6/417379790_892614632321109_331978589442030329_n.jpg?stp=cp6_dst-jpg&_nc_cat=104&ccb=1-7&_nc_sid=5f2048&_nc_ohc=BCPdUFeZuqoQ7kNvgFT7nt-&_nc_ht=scontent-zrh1-1.xx&oh=00_AfA0SUayO8Bt2y2LQJLNHaL8CP7NSV5ChMfmQuuP5fxrHA&oe=663AC822"))

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("TicketScreen"),
      topBar = { PageTitleWithGoBack(ticket.name, navigationActions) }) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(paddingValues).testTag("TicketDetails"),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Image(
              painter = rememberAsyncImagePainter(ticket.banner),
              contentDescription = null,
              contentScale = ContentScale.Crop,
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(horizontal = 16.dp)
                      .height(150.dp)
                      .clip(shape = RoundedCornerShape(20.dp))
                      .background(Color.Gray))
          Spacer(modifier = Modifier.height(8.dp))

          Image(
              painter = rememberAsyncImagePainter(""),
              contentDescription = null,
              contentScale = ContentScale.Crop,
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(horizontal = 16.dp)
                      .height(350.dp)
                      .clip(shape = RoundedCornerShape(20.dp))
                      .background(Color.Gray))

          Spacer(modifier = Modifier.height(8.dp))

          Text(text = "Start Time: ${ticket.startTime}", modifier = Modifier.padding(8.dp))
          Text(text = "End Time: ${ticket.startTime}", modifier = Modifier.padding(8.dp))
          Text(text = "Description: ${ticket.description}", modifier = Modifier.padding(8.dp))
        }
      }
}
